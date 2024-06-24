package dev.huskuraft.effortless.forge.platform;

import dev.huskuraft.effortless.api.platform.Mod;
import net.neoforged.neoforge.fml.loading.moddiscovery.ModInfo;

record ForgeMod(ModInfo modInfo) implements Mod {

    @Override
    public String getId() {
        return modInfo().getModId();
    }

    @Override
    public String getVersionStr() {
        return modInfo().getVersion().toString();
    }

    @Override
    public String getDescription() {
        return modInfo().getDescription();
    }

    @Override
    public String getName() {
        return modInfo().getDisplayName();
    }

}
