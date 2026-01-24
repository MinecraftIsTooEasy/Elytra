package com.inf1nlty.elytra.mixin.entity;

import com.inf1nlty.elytra.ElytraInit;

import net.minecraft.EntityDragon;
import net.minecraft.EntityItem;
import net.minecraft.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDragon.class)
public class EntityDragonMixin {

    @Inject(method = "onDeathUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityDragon;createEnderPortal(II)V", shift = At.Shift.BEFORE))
    private void elytra$dropTemplate(CallbackInfo ci) {
        EntityDragon dragon = (EntityDragon)(Object)this;

        if (!dragon.worldObj.isRemote && dragon.deathTicks == 200) {
            EntityItem entityItem = new EntityItem(
                    dragon.worldObj,
                    dragon.posX,
                    dragon.posY,
                    dragon.posZ,
                    new ItemStack(ElytraInit.ELYTRA_TEMPLATE, 1)
            );
            dragon.worldObj.spawnEntityInWorld(entityItem);
        }
    }
}