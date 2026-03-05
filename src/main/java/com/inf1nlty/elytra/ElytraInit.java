package com.inf1nlty.elytra;

import net.minecraft.CraftingManager;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.minecraft.CreativeModeTab;
import net.minecraft.ShapedRecipes;
import net.minecraft.ShapelessRecipes;

import java.util.ArrayList;
import java.util.List;

public final class ElytraInit {

    private static final int ELYTRA_ID = 12356;
    private static final int ELYTRA_TEMPLATE_ID = 12357;

    public static ItemElytra ELYTRA;
    public static Item ELYTRA_TEMPLATE;

    private static boolean initialized = false;

    @SuppressWarnings("unchecked")
    public static void register() {
        if (initialized) return;
        initialized = true;

        ELYTRA = new ItemElytra(ELYTRA_ID);
        ELYTRA_TEMPLATE = new ItemElytraTemplate(ELYTRA_TEMPLATE_ID);

        ELYTRA.setUnlocalizedName("elytra");
        ELYTRA_TEMPLATE.setUnlocalizedName("elytra_template");

        // Shaped recipe: "LSL","LTL"," F " (L=leather, S=silk, T=template, F=feather)
        ItemStack[] shapedInput = new ItemStack[9];
        shapedInput[0] = new ItemStack(Item.leather); // L
        shapedInput[1] = new ItemStack(Item.silk);    // S
        shapedInput[2] = new ItemStack(Item.leather); // L
        shapedInput[3] = new ItemStack(Item.leather); // L
        shapedInput[4] = new ItemStack(ELYTRA_TEMPLATE); // T
        shapedInput[5] = new ItemStack(Item.leather); // L
        shapedInput[6] = null;                        // ' '
        shapedInput[7] = new ItemStack(Item.feather); // F
        shapedInput[8] = null;                        // ' '

        ShapedRecipes shaped = new ShapedRecipes(3, 3, shapedInput, new ItemStack(ELYTRA), true);

        // Shapeless: template + writableBook -> template x2
        List<ItemStack> shapelessIngredients = new ArrayList<>();
        shapelessIngredients.add(new ItemStack(ELYTRA_TEMPLATE));
        shapelessIngredients.add(new ItemStack(Item.writableBook));
        ShapelessRecipes shapeless = new ShapelessRecipes(new ItemStack(ELYTRA_TEMPLATE, 2), shapelessIngredients, true);

        CraftingManager cm = CraftingManager.getInstance();
        cm.getRecipeList().add(shaped);
        cm.getRecipeList().add(shapeless);
    }
}