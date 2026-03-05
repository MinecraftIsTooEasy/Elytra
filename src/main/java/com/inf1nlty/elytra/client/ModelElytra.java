package com.inf1nlty.elytra.client;

import net.minecraft.bbo;
import net.minecraft.bcu;

public class ModelElytra extends bbo {
    private final bcu rightWing;
    private final bcu leftWing;

    public ModelElytra() {
        this.t = 64;
        this.u = 32;

        this.rightWing = new bcu(this, 22, 0);
        this.rightWing.i = true;
        this.rightWing.a(0.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.rightWing.a(-5.0F, 0.0F, 0.0F);

        this.leftWing = new bcu(this, 22, 0);
        this.leftWing.a(-10.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.leftWing.a(5.0F, 0.0F, 0.0F);
    }

    public void setupAnim(boolean flying, boolean crouching, float pitch) {
        float xRot = 0.2617994F;
        float zRot = -0.2617994F;
        float yOffset = 0.0F;
        float yRot = 0.0F;

        if (flying) {

            xRot = 0.34906584F;
            zRot = (float)(-Math.PI / 2.0);

            if (pitch > 0.0F) {
                float pitchRad = pitch * ((float)Math.PI / 180F);
                float diveFactor = Math.min(pitchRad / (float)(Math.PI / 2.0), 1.0F);

                xRot += diveFactor * 0.52F;
                zRot = (float)(-Math.PI / 2.0) + diveFactor * 0.87F;
            }

            else if (pitch < 0.0F) {
                float pitchRad = -pitch * ((float)Math.PI / 180F);
                float climbFactor = Math.min(pitchRad / (float)(Math.PI / 4.0), 1.0F);

                xRot -= climbFactor * 0.17F;
                zRot = (float)(-Math.PI / 2.0) - climbFactor * 0.26F;
            }
        } else if (crouching) {
            xRot = 0.6981317F;
            zRot = (float)(-Math.PI / 4.0);
            yOffset = 3.0F;
            yRot = 0.08726646F;
        }

        this.leftWing.p = yOffset;
        this.leftWing.f = xRot;
        this.leftWing.g = yRot;
        this.leftWing.h = zRot;

        this.rightWing.p = yOffset;
        this.rightWing.f = xRot;
        this.rightWing.g = -yRot;
        this.rightWing.h = -zRot;
    }

    public void render(float scale) {
        this.rightWing.a(scale);
        this.leftWing.a(scale);
    }
}