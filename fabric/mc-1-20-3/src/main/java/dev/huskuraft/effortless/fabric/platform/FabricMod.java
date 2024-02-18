package dev.huskuraft.effortless.fabric.platform;

import dev.huskuraft.effortless.api.platform.Mod;
import net.fabricmc.loader.api.ModContainer;

record FabricMod(ModContainer modContainer) implements Mod {

    @Override
    public String getId() {
        return modContainer().getMetadata().getId();
    }

    @Override
    public String getVersionStr() {
        return modContainer().getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String getDescription() {
        return modContainer().getMetadata().getDescription();
    }

    @Override
    public String getName() {
        return modContainer().getMetadata().getName();
    }

}
