package com.inf1nlty.elytra.mixin.client;

import com.inf1nlty.elytra.client.RenderElytra;

import net.minecraft.AbstractClientPlayer;
import net.minecraft.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class RenderPlayerMixin {

    @Inject(method = "renderSpecials", at = @At("RETURN"))
    private void elytra$renderElytra(AbstractClientPlayer player, float partialTicks, CallbackInfo ci) {
        RenderElytra.renderElytra(player, partialTicks);
    }
}