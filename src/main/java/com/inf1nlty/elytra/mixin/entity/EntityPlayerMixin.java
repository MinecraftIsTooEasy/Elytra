package com.inf1nlty.elytra.mixin.entity;

import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.ItemElytra;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {

    @Inject(method = "onUpdate", at = @At("RETURN"))
    private void elytra$onUpdate(CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer)(Object)this;

        if (!ElytraPhysics.getElytraFlying(self)) return;

        ItemStack chest = self.inventory.armorItemInSlot(2);
        boolean valid = chest != null && chest.getItem() instanceof ItemElytra && !ItemElytra.isBroken(chest);

        if (valid) {
            ElytraPhysics.handleFlightPhysics(self, chest);
        } else {
            ElytraPhysics.setElytraFlying(self, false);
            ElytraPhysics.setTicksElytraFlying(self, 0);
        }
    }
}