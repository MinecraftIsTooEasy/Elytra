package com.inf1nlty.elytra.mixin.network;

import com.inf1nlty.elytra.ElytraNetwork;
import com.inf1nlty.elytra.mixin.accessor.NetServerHandlerAccessor;

import net.minecraft.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin {

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void elytra$handlePacket(Packet250CustomPayload packet, CallbackInfo ci) {
        if (packet == null) return;

        NetServerHandler handler = (NetServerHandler)(Object)this;
        ServerPlayer player = ((NetServerHandlerAccessor)handler).getPlayerEntity();

        if (player == null) return;

        String channel = packet.channel;
        if (ElytraNetwork.CHANNEL_START.equals(channel)) {
            ElytraNetwork.handleStartPacket(player);
            ci.cancel();

        } else if (ElytraNetwork.CHANNEL_STOP.equals(channel)) {
            ElytraNetwork.handleStopPacket(player);
            ci.cancel();
        }
    }
}