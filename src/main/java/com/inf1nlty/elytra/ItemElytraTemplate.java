package com.inf1nlty.elytra;

import net.minecraft.*;

import java.util.List;

public class ItemElytraTemplate extends Item {

    public ItemElytraTemplate(int id) {
        super(id, Material.adamantium, "elytra_template");
        this.setMaxStackSize(16);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        info.add(EnumChatFormatting.GRAY + Translator.getFormatted("item.tooltip.elytra_template", new Object[0]));
    }
}