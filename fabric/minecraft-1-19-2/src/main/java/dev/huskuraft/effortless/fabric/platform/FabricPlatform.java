package dev.huskuraft.effortless.fabric.platform;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.vanilla.platform.MinecraftCommonContentFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.nio.file.Path;

public class FabricPlatform implements Platform {

    public static final FabricPlatform INSTANCE = new FabricPlatform();

    @Override
    public String getLoaderName() {
        return "Fabric-Official";
    }

    @Override
    public String getLoaderVersion() {
        return FabricLoaderImpl.VERSION;
    }

    @Override
    public String getGameVersion() {
        return FabricLoaderImpl.INSTANCE.getGameProvider().getRawGameVersion();
    }

    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Environment getEnvironment() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> Environment.CLIENT;
            case SERVER -> Environment.SERVER;
        };
    }

    @Override
    public boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

}
