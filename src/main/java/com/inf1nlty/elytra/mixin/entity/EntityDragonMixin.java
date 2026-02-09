package com.inf1nlty.elytra.mixin.entity;

import com.inf1nlty.elytra.ElytraInit;

import net.minecraft.EntityDragon;
import net.minecraft.EntityItem;
import net.minecraft.ItemStack;

import net.minecraft.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDragon.class)
public class EntityDragonMixin {

    @Inject(method = "onDeathUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityDragon;createEnderPortal(II)V", shift = At.Shift.BEFORE))
    private void elytra$dropTemplate(CallbackInfo ci) {
        EntityDragon dragon = (EntityDragon)(Object)this;

        if (!dragon.worldObj.isRemote && dragon.deathTicks >= 200 && dragon.deathTicks <= 201) {
            if (ElytraInit.ELYTRA_TEMPLATE == null) return;

            int eggX = MathHelper.floor_double(dragon.posX);
            int eggZ = MathHelper.floor_double(dragon.posZ);
            int eggY = 64 + 4;

            double spawnX = eggX + 0.5D;
            double spawnY = eggY + 1.5D;
            double spawnZ = eggZ + 0.5D;

            EntityItem entityItem = new EntityItem(
                    dragon.worldObj,
                    spawnX,
                    spawnY,
                    spawnZ,
                    new ItemStack(ElytraInit.ELYTRA_TEMPLATE, 1)
            );

            entityItem.motionX = 0.0D;
            entityItem.motionY = 0.35D;
            entityItem.motionZ = 0.0D;
            entityItem.delayBeforeCanPickup = 40;

            dragon.worldObj.spawnEntityInWorld(entityItem);
        }
    }
}