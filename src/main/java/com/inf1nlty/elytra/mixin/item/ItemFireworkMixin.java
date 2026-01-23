package com.inf1nlty.elytra.mixin.item;

import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.IEntityFireworkRocket;

import net.minecraft.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFirework.class)
public class ItemFireworkMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    private void elytra$boostElytra(EntityPlayer player, float partialTick, boolean ctrlIsDown, CallbackInfoReturnable<Boolean> cir) {

        if (!ElytraPhysics.getElytraFlying(player)) return;

        ItemStack itemStack = player.inventory.getCurrentItemStack();
        if (itemStack == null) return;

        World world = player.worldObj;
        double x = player.posX;
        double y = player.posY + player.getEyeHeight();
        double z = player.posZ;

        EntityFireworkRocket rocket = new EntityFireworkRocket(world, x, y, z, itemStack.copy());
        world.spawnEntityInWorld(rocket);

        if (rocket instanceof IEntityFireworkRocket) {
            ((IEntityFireworkRocket) rocket).elytra$setAttachedPlayerId(player.entityId);
        }

        if (!world.isRemote && !player.capabilities.isCreativeMode) {
            itemStack.stackSize--;
        }

        cir.setReturnValue(true);
    }
}