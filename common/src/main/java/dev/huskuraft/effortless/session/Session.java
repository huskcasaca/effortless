package dev.huskuraft.effortless.session;

import java.util.List;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.LoaderType;
import dev.huskuraft.effortless.api.platform.Mod;
import dev.huskuraft.effortless.api.platform.Platform;

public record Session(
        LoaderType loaderType,
        String loaderVersion,
        String gameVersion,
        List<Mod> mods,
        int protocolVersion
) {

    public static Session current() {
        var platform = Platform.getInstance();
        var protocolVersion = Effortless.PROTOCOL_VERSION;
        return new Session(
                platform.getLoaderType(),
                platform.getLoaderVersion(),
                platform.getGameVersion(),
                platform.getRunningMods(),
                protocolVersion
        );
    }

}
