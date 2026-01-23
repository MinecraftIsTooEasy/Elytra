package com.inf1nlty.elytra;

import net.minecraft.*;

public class ItemElytra extends ItemArmor {

    public ItemElytra(int id) {
        super(id, Material.leather, 1, false);
        this.setMaxStackSize(1);
        this.setMaxDamage(432);
    }

    @Override
    public String getArmorType() {
        return "chestplate";
    }

    @Override
    public int getNumComponentsForDurability() {
        return 8;
    }

    @Override
    public int getMaterialProtection() {
        return 0;
    }

    public static final DamageSource flyIntoWall = DamageSource.inWall;

    public static boolean isBroken(ItemStack stack) {
        return stack.getItemDamage() >= stack.getMaxDamage() - 1;
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partial_tick, boolean ctrl_is_down) {
        ItemStack item_stack = player.getHeldItemStack();
        int index = 2;
        ItemStack equipped = player.getCurrentArmor(index);
        if (equipped == null) {
            if (player.onServer()) {
                player.setCurrentItemOrArmor(index, item_stack.copy());
                player.convertOneOfHeldItem(null);
                player.suppressNextStatIncrement();
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack item_stack, EntityPlayer player, java.util.List info, boolean extended_info, Slot slot) {
        if (extended_info && item_stack.isItemDamaged()) {
            int remaining = item_stack.getMaxDamage() - item_stack.getItemDamage();
            int max = item_stack.getMaxDamage();
            info.add(StatCollector.translateToLocalFormatted("item.elytra.tooltip", remaining, max));
        }
    }
}