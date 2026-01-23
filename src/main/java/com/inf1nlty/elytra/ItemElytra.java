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

    @Override
    public Item getRepairItem() {
        return Item.leather;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return getColorStatic(stack) != -1;
    }

    @Override
    public int getColor(ItemStack stack) {
        return getColorStatic(stack);
    }

    public static int getColorStatic(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) return -1;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("display")) return -1;
        NBTTagCompound display = nbt.getCompoundTag("display");
        return display.hasKey("color") ? display.getInteger("color") : -1;
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
}