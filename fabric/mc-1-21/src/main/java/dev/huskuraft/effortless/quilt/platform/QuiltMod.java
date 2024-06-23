package dev.huskuraft.effortless.quilt.platform;

import org.quiltmc.loader.api.ModContainer;

import dev.huskuraft.effortless.api.platform.Mod;

record QuiltMod(ModContainer modContainer) implements Mod {

    @Override
    public String getId() {
        return modContainer().metadata().id();
    }

    @Override
    public String getVersionStr() {
        return modContainer().metadata().version().raw();
    }

    @Override
    public String getDescription() {
        return modContainer().metadata().description();
    }

    @Override
    public String getName() {
        return modContainer().metadata().name();
    }

}
