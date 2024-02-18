package dev.huskuraft.effortless.forge.platform;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.platform.Environment;
import dev.huskuraft.effortless.api.platform.LoaderType;
import dev.huskuraft.effortless.api.platform.Mod;
import dev.huskuraft.effortless.api.platform.Platform;
import net.minecraftforge.fml.loading.FMLLoader;

@AutoService(Platform.class)
public class ForgePlatform implements Platform {

    @Override
    public LoaderType getLoaderType() {
        return LoaderType.FORGE;
    }

    @Override
    public String getLoaderVersion() {
        return FMLLoader.versionInfo().forgeVersion();
    }

    @Override
    public String getGameVersion() {
        return FMLLoader.versionInfo().mcVersion();
    }

    @Override
    public List<Mod> getRunningMods() {
        return FMLLoader.getLoadingModList().getMods().stream().map(ForgeMod::new).collect(Collectors.toList());
    }

    @Override
    public Path getGameDir() {
        return FMLLoader.getGamePath();
    }

    @Override
    public Environment getEnvironment() {
        return switch (FMLLoader.getDist()) {
            case CLIENT -> Environment.CLIENT;
            case DEDICATED_SERVER -> Environment.SERVER;
        };
    }

    @Override
    public boolean isDevelopment() {
        return !FMLLoader.isProduction();
    }


}
