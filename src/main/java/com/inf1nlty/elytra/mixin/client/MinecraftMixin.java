package com.inf1nlty.elytra.mixin.client;

import com.inf1nlty.elytra.client.ElytraClientHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "runTick", at = @At("RETURN"))
    private void elytra$onRunTickEnd(CallbackInfo ci) {
        try {
            ElytraClientHandler.onClientTick();
        } catch (Throwable ignored) {}
    }
}
