package com.inf1nlty.elytra.mixin.accessor;

import net.minecraft.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("width")
    void setWidth(float width);

    @Accessor("height")
    void setHeight(float height);
}