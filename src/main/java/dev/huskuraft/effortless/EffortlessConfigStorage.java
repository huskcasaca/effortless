package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.file.ConfigFileStorage;
import dev.huskuraft.universal.api.file.FileType;
import dev.huskuraft.effortless.session.config.SessionConfig;
import dev.huskuraft.effortless.session.config.serializer.SessionConfigSerializer;

public final class EffortlessConfigStorage extends ConfigFileStorage<SessionConfig> {

    public static final String CONFIG_NAME = "effortless.toml";

    public EffortlessConfigStorage(Effortless entrance) {
        super(CONFIG_NAME, FileType.TOML, new SessionConfigSerializer());
    }

}
