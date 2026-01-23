package com.inf1nlty.elytra.mixin.client;

import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.ItemElytra;
import com.inf1nlty.elytra.client.RenderElytra;

import net.minecraft.ItemStack;
import net.minecraft.RenderPlayer;
import net.minecraft.AbstractClientPlayer;
import net.minecraft.EntityLivingBase;

import org.lwjgl.opengl.GL11;
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

    @Inject(method = "rotateCorpse", at = @At("RETURN"))
    private void elytra$applyFlyingRotation(EntityLivingBase entity, float p1, float p2, float partialTicks, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player)) return;

        if (!ElytraPhysics.getElytraFlying(player)) return;

        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

        float pitch = player.rotationPitch;
        float clampedPitch = Math.max(-60.0F, Math.min(15.0F, pitch));
        GL11.glRotatef(-clampedPitch, 1.0F, 0.0F, 0.0F);
    }


    @Inject(method = "renderLivingAt", at = @At("RETURN"))
    private void elytra$adjustFlyingPosition(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player)) return;

        if (!ElytraPhysics.getElytraFlying(player)) return;

        GL11.glTranslated(0.0, -0.35, 0.0);
    }
}