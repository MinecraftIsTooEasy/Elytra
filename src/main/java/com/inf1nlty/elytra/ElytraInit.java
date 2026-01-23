package com.inf1nlty.elytra;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.minecraft.CreativeTabs;

import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class ElytraInit {

    public static ItemElytra ELYTRA;
    public static Item ELYTRA_TEMPLATE;

    public static void registerItems(ItemRegistryEvent event) {

        int itemId = IdUtil.getNextItemID();
        ELYTRA = new ItemElytra(itemId);
        event.register("Elytra", "elytra:elytra", "elytra", ELYTRA);

        int templateId = IdUtil.getNextItemID();
        ELYTRA_TEMPLATE = new Item(templateId, Material.adamantium, "elytra_template") {};
        ELYTRA_TEMPLATE.setMaxStackSize(16);
        ELYTRA_TEMPLATE.setCreativeTab(CreativeTabs.tabMaterials);

        event.register("Elytra", "elytra:elytra_template", "elytra_template", ELYTRA_TEMPLATE);
    }

    public static void registerRecipes(RecipeRegistryEvent event) {
        event.registerShapedRecipe(
                new ItemStack(ELYTRA),
                true,
                "LSL",
                "LTL",
                " F ",
                'L', Item.leather,
                'F', Item.feather,
                'S', Item.silk,
                'T', new ItemStack(ELYTRA_TEMPLATE)
        );

        event.registerShapelessRecipe(
                new ItemStack(ELYTRA_TEMPLATE, 2),
                true,
                new ItemStack(ELYTRA_TEMPLATE),
                new ItemStack(Item.writableBook)
        );
    }
}