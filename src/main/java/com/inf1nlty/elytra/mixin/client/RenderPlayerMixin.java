package com.inf1nlty.elytra.mixin.client;

import com.inf1nlty.elytra.ItemElytra;
import com.inf1nlty.elytra.client.RenderElytra;

import net.minecraft.ItemStack;
import net.minecraft.RenderPlayer;
import net.minecraft.AbstractClientPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderPlayer.class)
public class RenderPlayerMixin {

    @Inject(method = "renderSpecials", at = @At("RETURN"))
    private void elytra$renderElytra(AbstractClientPlayer player, float partialTicks, CallbackInfo ci) {
        RenderElytra.renderElytra(player);
    }

    @Inject(method = "setArmorModel", at = @At("HEAD"), cancellable = true)
    private void elytra$skipArmorSetup(AbstractClientPlayer player, int armorSlot, float partialTicks, CallbackInfoReturnable<Integer> cir) {
        if (armorSlot == 1) {
            ItemStack chest = player.inventory.armorItemInSlot(2);
            if (chest != null && chest.getItem() instanceof ItemElytra) {
                cir.setReturnValue(-1);
            }
        }
    }
}