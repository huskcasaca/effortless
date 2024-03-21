package dev.huskuraft.effortless.session;

import java.util.List;

import dev.huskuraft.effortless.api.platform.LoaderType;
import dev.huskuraft.effortless.api.platform.Mod;
import dev.huskuraft.effortless.session.config.SessionConfig;

public record Session(
        LoaderType loaderType,
        String loaderVersion,
        String gameVersion,
        List<Mod> mods,
        int protocolVersion,
        SessionConfig config
) {

}
