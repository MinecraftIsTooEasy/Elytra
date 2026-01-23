package com.inf1nlty.elytra;

import net.minecraft.*;

public final class ElytraNetwork {

    public static final String CHANNEL_START = "elytra:start";
    public static final String CHANNEL_STOP = "elytra:stop";

    private ElytraNetwork() {}

    public static void register() {
    }

    public static void handleStartPacket(EntityPlayer player) {
        if (player == null) return;

        ItemStack stack = player.inventory.armorItemInSlot(2);
        if (stack != null
                && stack.getItem() instanceof ItemElytra
                && !ItemElytra.isBroken(stack)
                && !player.onGround
                && !player.isInWater()
                && player.motionY < 0.0D) {

            ElytraPhysics.setElytraFlying(player, true);
            ElytraPhysics.setTicksElytraFlying(player, 0);
        }
    }

    public static void handleStopPacket(EntityPlayer player) {
        if (player == null) return;

        ElytraPhysics.setElytraFlying(player, false);
        ElytraPhysics.setTicksElytraFlying(player, 0);
    }
}