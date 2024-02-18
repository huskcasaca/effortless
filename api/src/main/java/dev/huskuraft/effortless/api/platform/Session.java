package dev.huskuraft.effortless.api.platform;

import java.util.List;

public record Session(
        LoaderType loaderType,
        String loaderVersion,
        String gameVersion,
        List<Mod> mods
) {

    public Session(Platform platform) {
        this(
                platform.getLoaderType(),
                platform.getLoaderVersion(),
                platform.getGameVersion(),
                platform.getRunningMods()
        );
    }

}
