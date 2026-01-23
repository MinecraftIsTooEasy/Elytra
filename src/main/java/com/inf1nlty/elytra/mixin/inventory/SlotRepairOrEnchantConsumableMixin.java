package com.inf1nlty.elytra.mixin.inventory;

import com.inf1nlty.elytra.ItemElytra;

import net.minecraft.*;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlotRepairOrEnchantConsumable.class)
public abstract class SlotRepairOrEnchantConsumableMixin extends Slot {

    public SlotRepairOrEnchantConsumableMixin(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    private void elytra$allowLeatherForElytra(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack == null) return;
        if (itemStack.getItem() != Item.leather) return;

        if (this.inventory == null) return;

        ItemStack firstSlot = this.inventory.getStackInSlot(0);
        if (firstSlot != null && firstSlot.getItem() instanceof ItemElytra) {
            cir.setReturnValue(true);
        }
    }
}