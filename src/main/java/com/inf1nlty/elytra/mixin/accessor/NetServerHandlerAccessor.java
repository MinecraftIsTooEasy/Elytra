package com.inf1nlty.elytra.mixin.accessor;

import net.minecraft.PlayerConnection;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerConnection.class)
public interface NetServerHandlerAccessor {

    @Accessor("playerEntity")
    ServerPlayer getPlayerEntity();
}