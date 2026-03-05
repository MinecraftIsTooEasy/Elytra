package com.inf1nlty.elytra;

import com.inf1nlty.elytra.mixin.MixinPackageMarker;

import net.xiaoyu233.fml.AbstractMod;
import net.xiaoyu233.fml.classloading.Mod;
import net.xiaoyu233.fml.config.InjectionConfig;

import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nonnull;

@Mod
public class ElytraMod extends AbstractMod {

    public static final String NAMESPACE = "elytra";

    @Override
    public void preInit() {
        ElytraNetwork.register();
    }

    @Nonnull
    @Override
    public InjectionConfig getInjectionConfig() {
        return InjectionConfig.Builder.of(NAMESPACE, MixinPackageMarker.class.getPackage(), MixinEnvironment.Phase.INIT).setRequired().build();
    }

    @Override
    public String modId() {
        return "Elytra";
    }

    @Override
    public int modVerNum() {
        return 100;
    }

    @Override
    public String modVerStr() {
        return "v1.0.0";
    }
}