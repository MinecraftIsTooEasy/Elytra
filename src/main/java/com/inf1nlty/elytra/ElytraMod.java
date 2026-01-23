package com.inf1nlty.elytra;

import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;

public class ElytraMod implements ModInitializer {
    public static final String NAMESPACE = "elytra";

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(NAMESPACE);

        MITEEvents.MITE_EVENT_BUS.register(new ElytraEventListener());

        ElytraNetwork.register();
    }
}