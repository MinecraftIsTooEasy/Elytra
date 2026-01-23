package com.inf1nlty.elytra;

import com.inf1nlty.elytra.mixin.accessor.EntityAccessor;
import net.minecraft.Damage;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.Vec3;

import java.util.HashMap;
import java.util.Map;

public class ElytraPhysics {

    /** Gravity coefficient (default 0.08) - The larger the value, the faster the descent */

    private static final double GRAVITY = 0.01;

    /** Pitch-up lift coefficient (default 0.1) - The larger the value, the faster the pitch-up */

    private static final double LIFT_FACTOR = 0.1;

    /** Dive acceleration coefficient - The larger the value, the faster the dive acceleration */

    private static final double DIVE_ACCELERATION = 0.01;

    /** Air drag - The smaller the value, the faster the deceleration */

    private static final double AIR_DRAG_XZ = 0.99;

    private static final double AIR_DRAG_Y = 0.78;

    /** Steering sensitivity - The larger the value, the faster the steering */

    private static final double STEERING_SENSITIVITY = 0.1;

    private static final Map<EntityPlayer, Boolean> flyingPlayers = new HashMap<>();
    private static final Map<EntityPlayer, Integer> flyingTicks = new HashMap<>();

    public static boolean getElytraFlying(EntityPlayer player) {
        return flyingPlayers.getOrDefault(player, false);
    }

    public static void setElytraFlying(EntityPlayer player, boolean flying) {
        flyingPlayers.put(player, flying);
        if (!flying) {
            flyingTicks.put(player, 0);
            EntityAccessor accessor = (EntityAccessor) player;
            accessor.setHeight(1.8F);
            accessor.setWidth(0.6F);
        }
    }

    public static int getTicksElytraFlying(EntityPlayer player) {
        return flyingTicks.getOrDefault(player, 0);
    }

    public static void setTicksElytraFlying(EntityPlayer player, int ticks) {
        flyingTicks.put(player, ticks);
    }

    public static void handleFlightPhysics(EntityPlayer player, ItemStack stack) {
        int ticks = getTicksElytraFlying(player) + 1;
        setTicksElytraFlying(player, ticks);

        player.fallDistance = 0.0F;

        EntityAccessor accessor = (EntityAccessor) player;
        accessor.setHeight(0.6F);
        accessor.setWidth(0.6F);

        Vec3 motion = Vec3.createVectorHelper(player.motionX, player.motionY, player.motionZ);
        Vec3 look = player.getLookVec();
        float pitch = player.rotationPitch * ((float)Math.PI / 180F);

        double lookXZ = Math.sqrt(look.xCoord * look.xCoord + look.zCoord * look.zCoord);
        double motionXZ = Math.sqrt(motion.xCoord * motion.xCoord + motion.zCoord * motion.zCoord);
        double lookLen = Math.sqrt(look.xCoord * look.xCoord + look.yCoord * look.yCoord + look.zCoord * look.zCoord);

        double cosPitch = Math.cos(pitch);
        cosPitch = cosPitch * cosPitch * Math.min(1.0, lookLen / 0.4);

        // Apply gravity (adjustable)
        motion = motion.addVector(0.0, GRAVITY * (-1.0 + cosPitch * 0.75), 0.0);

        // Head-up lift (adjustable)
        if (motion.yCoord < 0.0 && lookXZ > 0.0) {
            double lift = motion.yCoord * -LIFT_FACTOR * cosPitch;
            motion = motion.addVector(
                    look.xCoord * lift / lookXZ,
                    lift,
                    look.zCoord * lift / lookXZ
            );
        }

        // Steering sensitivity (adjustable)
        if (lookXZ > 0.0) {
            motion = motion.addVector(
                    (look.xCoord / lookXZ * motionXZ - motion.xCoord) * STEERING_SENSITIVITY,
                    0.0,
                    (look.zCoord / lookXZ * motionXZ - motion.zCoord) * STEERING_SENSITIVITY
            );
        }

        // Apply air resistance FIRST
        motion = Vec3.createVectorHelper(
                motion.xCoord * AIR_DRAG_XZ,
                motion.yCoord * AIR_DRAG_Y,
                motion.zCoord * AIR_DRAG_XZ
        );

        // Pitch > 0 = Looking DOWN = Dive acceleration
        if (pitch > 0.0F) {
            double pitchDegrees = Math.toDegrees(pitch);
            double diveAngle = Math.sin(pitch);
            double totalSpeed = Math.sqrt(motion.xCoord * motion.xCoord + motion.yCoord * motion.yCoord + motion.zCoord * motion.zCoord);

            double diveCoefficient;
            if (pitchDegrees < 45.0) {
                // Shallow dive (0° - 45°) - Gentle acceleration
                diveCoefficient = 0.10;
            } else {
                // Steep dive (45° - 90°) - Aggressive acceleration
                diveCoefficient = 0.15;
            }

            double diveBoost = totalSpeed * diveAngle * diveCoefficient;
            motion = motion.addVector(0.0, -diveBoost, 0.0);
        }

        // Pitch < 0 = Looking UP = Climb resistance
        if (pitch < 0.0F && motion.yCoord < 0.0) {
            double pitchDegrees = Math.abs(Math.toDegrees(pitch));
            double climbAngle = -Math.sin(pitch);

            double climbCoefficient;
            if (pitchDegrees < 30.0) {
                // Slight pitch-up (0° - 30°) - Weak resistance
                climbCoefficient = 0.3;
            } else {
                // Strong pitch-up (30° - 90°) - Strong resistance
                climbCoefficient = 0.7;
            }

            double climbBoost = Math.abs(motion.yCoord) * climbAngle * climbCoefficient;
            motion = motion.addVector(0.0, climbBoost, 0.0);
        }

        // Apply final motion
        player.motionX = motion.xCoord;
        player.motionY = motion.yCoord;
        player.motionZ = motion.zCoord;

        player.moveEntity(player.motionX, player.motionY, player.motionZ);

        // Wall collision damage detection
        if (player.isCollidedHorizontally && !player.worldObj.isRemote) {
            double newSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
            double speedLoss = motionXZ - newSpeed;
            float damage = (float)(speedLoss * 10.0 - 3.0);

            if (damage > 0.0F) {
                player.attackEntityFrom(new Damage(ItemElytra.flyIntoWall, damage));
            }
        }

        // Landing detection
        if (player.onGround && !player.worldObj.isRemote) {
            setElytraFlying(player, false);
            setTicksElytraFlying(player, 0);

            accessor.setHeight(1.8F);
            accessor.setWidth(0.6F);
        }

        // Water detection
        if (player.isInWater() && !player.worldObj.isRemote) {
            setElytraFlying(player, false);
            setTicksElytraFlying(player, 0);

            accessor.setHeight(1.8F);
            accessor.setWidth(0.6F);
        }

        if (!player.worldObj.isRemote && ticks % 20 == 0) {
            stack.tryDamageItem(player.worldObj, 1, false);
        }
    }
}
