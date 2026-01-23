package com.inf1nlty.elytra.mixin.entity;

import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.IEntityFireworkRocket;

import net.minecraft.EntityFireworkRocket;
import net.minecraft.NBTTagCompound;
import net.minecraft.EntityPlayer;
import net.minecraft.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityFireworkRocket.class)
public class EntityFireworkRocketMixin implements IEntityFireworkRocket {

    @Unique private int elytra$attachedPlayerId = -1;
    @Unique private int elytra$boostLifetime = 0;
    @Shadow public int fireworkAge;

    /** Rocket duration (default 60 ticks ≈ 3 seconds) */

    @Unique private static final int MAX_BOOST_LIFETIME = 60;

    /** Initial thrust (default 0.1) - Higher values result in faster initial velocity */

    @Unique private static final double ROCKET_IMPULSE = 0.1;

    /** Acceleration blending coefficient (default 0.5) - Higher values result in stronger sustained acceleration */

    @Unique private static final double ROCKET_BLEND = 0.5;

    /** Low-speed target velocity (default 0.3) - Initial thrust velocity */

    @Unique private static final double LOW_SPEED_TARGET = 0.3;

    /** Medium-speed target velocity (default 0.6) - Medium-speed thrust */

    @Unique private static final double MID_SPEED_TARGET = 0.6;

    /** High-speed target velocity (default 0.9) - /* Maximum propulsion speed */

    @Unique private static final double HIGH_SPEED_TARGET = 0.9;

    // ===========================================

    @Override
    @Unique
    public void elytra$setAttachedPlayerId(int playerId) {
        this.elytra$attachedPlayerId = playerId;
    }

    @Override
    @Unique
    public int elytra$getAttachedPlayerId() {
        return this.elytra$attachedPlayerId;
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void elytra$attachAndBoost(CallbackInfo ci) {
        EntityFireworkRocket self = (EntityFireworkRocket)(Object)this;

        if (elytra$attachedPlayerId != -1) {
            EntityPlayer target = elytra$findPlayerById(self, elytra$attachedPlayerId);

            if (target != null && ElytraPhysics.getElytraFlying(target) && elytra$boostLifetime < MAX_BOOST_LIFETIME) {
                elytra$applyBoost(self, target);
                elytra$boostLifetime++;

            } else {
                elytra$detachAndDestroy(self);
            }
        }
    }

    @Unique
    private EntityPlayer elytra$findPlayerById(EntityFireworkRocket rocket, int playerId) {
        for (Object o : rocket.worldObj.playerEntities) {
            if (o instanceof EntityPlayer) {
                EntityPlayer p = (EntityPlayer) o;
                if (p.entityId == playerId) return p;
            }
        }
        return null;
    }

    @Unique
    private void elytra$applyBoost(EntityFireworkRocket self, EntityPlayer target) {
        Vec3 look = target.getLookVec();
        Vec3 motion = Vec3.createVectorHelper(target.motionX, target.motionY, target.motionZ);

        double currentSpeed = Math.sqrt(
                motion.xCoord * motion.xCoord +
                        motion.yCoord * motion.yCoord +
                        motion.zCoord * motion.zCoord
        );

        double targetSpeed;
        if (currentSpeed < 0.3) {
            targetSpeed = LOW_SPEED_TARGET;
        } else if (currentSpeed < 1.0) {
            targetSpeed = MID_SPEED_TARGET;
        } else {
            targetSpeed = HIGH_SPEED_TARGET;
        }

        Vec3 boost = Vec3.createVectorHelper(
                look.xCoord * ROCKET_IMPULSE + (look.xCoord * targetSpeed - motion.xCoord) * ROCKET_BLEND,
                look.yCoord * ROCKET_IMPULSE + (look.yCoord * targetSpeed - motion.yCoord) * ROCKET_BLEND,
                look.zCoord * ROCKET_IMPULSE + (look.zCoord * targetSpeed - motion.zCoord) * ROCKET_BLEND
        );

        target.motionX += boost.xCoord;
        target.motionY += boost.yCoord;
        target.motionZ += boost.zCoord;

        self.setPosition(target.posX, target.posY - 1, target.posZ);
        self.motionX = 0;
        self.motionY = 0;
        self.motionZ = 0;

        if (elytra$boostLifetime == 0 && !self.worldObj.isRemote) {
            self.worldObj.playSoundAtEntity(self, "fireworks.launch", 3.0F, 1.0F);
        }
    }

    @Unique
    private void elytra$detachAndDestroy(EntityFireworkRocket self) {
        elytra$attachedPlayerId = -1;
        elytra$boostLifetime = 0;
        self.setDead();
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void elytra$writeNBT(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("ElytraAttachedPlayer", elytra$attachedPlayerId);
        nbt.setInteger("ElytraBoostLifetime", elytra$boostLifetime);
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void elytra$readNBT(NBTTagCompound nbt, CallbackInfo ci) {
        elytra$attachedPlayerId = nbt.getInteger("ElytraAttachedPlayer");
        elytra$boostLifetime = nbt.getInteger("ElytraBoostLifetime");
    }
}