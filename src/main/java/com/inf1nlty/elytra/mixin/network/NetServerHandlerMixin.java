package com.inf1nlty.elytra.mixin.network;

import com.inf1nlty.elytra.ElytraNetwork;

import net.minecraft.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetServerHandler.class)
public class NetServerHandlerMixin {

    @Shadow public EntityPlayer playerEntity;

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void elytra$handlePacket(Packet250CustomPayload packet, CallbackInfo ci) {
        if (packet.channel.equals(ElytraNetwork.CHANNEL_START)) {
            ElytraNetwork.handleStartPacket(playerEntity);
            ci.cancel();
        } else if (packet.channel.equals(ElytraNetwork.CHANNEL_STOP)) {
            ElytraNetwork.handleStopPacket(playerEntity);
            ci.cancel();
        }
    }
}
