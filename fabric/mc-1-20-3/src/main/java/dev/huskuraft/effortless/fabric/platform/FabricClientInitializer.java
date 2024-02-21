package dev.huskuraft.effortless.fabric.platform;

import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.fabric.events.PlatformLifecycleEvents;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientEntrance.getInstance();

        PlatformLifecycleEvents.CLIENT_START.invoker().onLaunch();
    }

}
