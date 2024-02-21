package dev.huskuraft.effortless.api.platform;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface Platform {

    static Platform getInstance() {
        return PlatformLoader.getSingleton();
    }

    LoaderType getLoaderType();

    String getLoaderVersion();

    String getGameVersion();

    List<Mod> getRunningMods();

    default Optional<Mod> findMod(String modId) {
        return getRunningMods().stream().filter(mod -> mod.getId().equals(modId)).findFirst();
    }

    Path getGameDir();

    default Path getConfigDir() {
        return getGameDir().resolve("config");
    }

    Environment getEnvironment();

    boolean isDevelopment();

}
