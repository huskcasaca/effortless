package dev.huskuraft.effortless.session;

import java.util.List;

import dev.huskuraft.universal.api.platform.LoaderType;
import dev.huskuraft.universal.api.platform.Mod;

public record Session(
        LoaderType loaderType,
        String loaderVersion,
        String gameVersion,
        List<Mod> mods,
        int protocolVersion
) {

}
