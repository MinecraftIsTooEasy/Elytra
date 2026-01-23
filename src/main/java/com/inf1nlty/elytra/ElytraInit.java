package com.inf1nlty.elytra;

import net.minecraft.CreativeTabs;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class ElytraInit {

    public static ItemElytra ELYTRA;

    public static void registerItems(ItemRegistryEvent event) {
        int itemId = IdUtil.getNextItemID();
        ELYTRA = new ItemElytra(itemId);

        event.register(ElytraMod.NAMESPACE, "elytra", ELYTRA, CreativeTabs.tabCombat);
    }

    public static void registerRecipes(RecipeRegistryEvent event) {
        event.registerShapedRecipe(
                new ItemStack(ELYTRA),
                true,
                "LSL",
                "LLL",
                " F ",
                'L', Item.leather,
                'F', Item.feather,
                'S', Item.silk
        );
    }
}