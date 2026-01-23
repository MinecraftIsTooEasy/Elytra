package com.inf1nlty.elytra.client;

import com.inf1nlty.elytra.ElytraPhysics;
import com.inf1nlty.elytra.ItemElytra;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderElytra {
    private static final ResourceLocation TEXTURE = new ResourceLocation("elytra:textures/entity/elytra.png");
    private static final ModelElytra MODEL = new ModelElytra();

    public static void renderElytra(EntityPlayer player, float partialTicks) {
        ItemStack chest = player.inventory.armorItemInSlot(2);
        if (chest == null || !(chest.getItem() instanceof ItemElytra)) return;

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

        GL11.glTranslatef(0.0F, 0.0F, 0.125F);

        boolean flying = ElytraPhysics.getElytraFlying(player);
        boolean crouching = player.isSneaking();

        MODEL.setupAnim(flying, crouching);

        MODEL.render(0.0625F);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}