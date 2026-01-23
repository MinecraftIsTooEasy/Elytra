package com.inf1nlty.elytra;

import com.google.common.eventbus.Subscribe;

import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;

public class ElytraEventListener {

    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        ElytraInit.registerItems(event);
    }

    @Subscribe
    public void onRecipeRegister(RecipeRegistryEvent event) {
        ElytraInit.registerRecipes(event);
    }
}