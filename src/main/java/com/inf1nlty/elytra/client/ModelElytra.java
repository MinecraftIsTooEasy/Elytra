package com.inf1nlty.elytra.client;

import net.minecraft.ModelBase;
import net.minecraft.ModelRenderer;

public class ModelElytra extends ModelBase {
    private final ModelRenderer rightWing;
    private final ModelRenderer leftWing;

    public ModelElytra() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        // Right
        this.rightWing = new ModelRenderer(this, 22, 0);
        this.rightWing.mirror = true;
        this.rightWing.addBox(0.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.rightWing.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.rightWing.rotateAngleX = 0.2617994F;
        this.rightWing.rotateAngleZ = 0.2617994F;

        // Left
        this.leftWing = new ModelRenderer(this, 22, 0);
        this.leftWing.addBox(-10.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.leftWing.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.leftWing.rotateAngleX = 0.2617994F;
        this.leftWing.rotateAngleZ = -0.2617994F;
    }

    public void setupAnim(boolean flying, boolean crouching) {
        float xRot = 0.2617994F;
        float zRot = -0.2617994F;
        float yOffset = 0.0F;
        float yRot = 0.0F;

        if (flying) {
            xRot = 0.34906584F;
            zRot = (float)(-Math.PI / 2.0);
        } else if (crouching) {
            xRot = 0.6981317F;
            zRot = (float)(-Math.PI / 4.0);
            yOffset = 3.0F;
            yRot = 0.08726646F;
        }

        this.leftWing.offsetY = yOffset;
        this.leftWing.rotateAngleX = xRot;
        this.leftWing.rotateAngleY = yRot;
        this.leftWing.rotateAngleZ = zRot;

        this.rightWing.offsetY = yOffset;
        this.rightWing.rotateAngleX = xRot;
        this.rightWing.rotateAngleY = -yRot;
        this.rightWing.rotateAngleZ = -zRot;
    }

    public void render(float scale) {
        this.rightWing.render(scale);
        this.leftWing.render(scale);
    }
}
