package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.file.ConfigFileStorage;
import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.building.config.RootConfig;
import dev.huskuraft.effortless.building.config.universal.RootSettingsConfigSerializer;

public final class EffortlessClientConfigStorage extends ConfigFileStorage<RootConfig> {

    private static final String CONFIG_NAME = "effortless-client.toml";

    public EffortlessClientConfigStorage(EffortlessClient entrance) {
        super(CONFIG_NAME, FileType.TOML, new RootSettingsConfigSerializer());
    }

}
