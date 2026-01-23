package com.inf1nlty.elytra.client;

import com.inf1nlty.elytra.ElytraNetwork;
import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.ItemElytra;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.EntityClientPlayerMP;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.Packet250CustomPayload;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ElytraClientHandler {

    private static final Map<String, Boolean> lastKeyDownMap = new HashMap<>();
    private static final Map<String, Boolean> lastFlyingState = new HashMap<>();

    private ElytraClientHandler() {}

    public static void onClientTick() {

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.thePlayer == null || mc.theWorld == null) return;

        EntityClientPlayerMP player = mc.thePlayer;
        ItemStack stack = player.inventory.armorItemInSlot(2);
        String playerKey = player.getCommandSenderName();

        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean lastKeyDown = lastKeyDownMap.getOrDefault(playerKey, false);
        boolean nowRising = keyDown && !lastKeyDown;

        boolean isFlying = ElytraPhysics.getElytraFlying(player);
        boolean wasFlying = lastFlyingState.getOrDefault(playerKey, false);

        if (nowRising && !player.onGround && player.motionY < 0.0D
                && !player.isInWater() && stack != null
                && stack.getItem() instanceof ItemElytra
                && !ItemElytra.isBroken(stack)) {

            sendPacket(mc, ElytraNetwork.CHANNEL_START);
            ElytraPhysics.setElytraFlying(player, true);
            ElytraPhysics.setTicksElytraFlying(player, 0);
        }

        if (isFlying) {
            boolean shouldStop = player.onGround
                    || player.isInWater()
                    || stack == null
                    || !(stack.getItem() instanceof ItemElytra)
                    || ItemElytra.isBroken(stack);

            if (shouldStop) {
                sendPacket(mc, ElytraNetwork.CHANNEL_STOP);
                ElytraPhysics.setElytraFlying(player, false);
                ElytraPhysics.setTicksElytraFlying(player, 0);
            }
        }

        lastKeyDownMap.put(playerKey, keyDown);
        lastFlyingState.put(playerKey, isFlying);
    }

    private static void sendPacket(Minecraft mc, String channel) {
        Packet250CustomPayload pkt = new Packet250CustomPayload(channel, new byte[0]);

        if (mc.getNetHandler() != null) {
            mc.getNetHandler().addToSendQueue(pkt);
        } else if (mc.thePlayer != null && mc.thePlayer.sendQueue != null) {
            mc.thePlayer.sendQueue.addToSendQueue(pkt);
        }
    }
}
