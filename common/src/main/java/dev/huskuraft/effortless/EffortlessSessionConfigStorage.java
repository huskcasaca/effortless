package dev.huskuraft.effortless;

import java.util.logging.Logger;

import com.electronwill.nightconfig.core.Config;

import dev.huskuraft.effortless.api.file.CommentedConfigFileStorage;
import dev.huskuraft.effortless.api.toml.CommentedConfigDeserializer;
import dev.huskuraft.effortless.api.toml.CommentedConfigSerializer;
import dev.huskuraft.effortless.session.SessionConfig;
import dev.huskuraft.effortless.session.SessionConfigSerDes;

public final class EffortlessSessionConfigStorage extends CommentedConfigFileStorage<SessionConfig> {

    private static final Logger LOGGER = Logger.getLogger("Effortless");
    private static final String CONFIG_NAME = "effortless.toml";
    private static final SessionConfigSerDes SESSION_CONFIG_SERIALIZER = new SessionConfigSerDes();
    private final Effortless entrance;
    private SessionConfig config;

    public EffortlessSessionConfigStorage(Effortless entrance) {
        this.entrance = entrance;
        Config.setInsertionOrderPreserved(true);
    }

    private Effortless getEntrance() {
        return entrance;
    }

    @Override
    public SessionConfig getDefault() {
        return SessionConfig.defaultConfig();
    }

    @Override
    public String getFileName() {
        return CONFIG_NAME;
    }

    @Override
    public CommentedConfigSerializer<SessionConfig> getSerializer() {
        return SESSION_CONFIG_SERIALIZER;
    }

    @Override
    public CommentedConfigDeserializer<SessionConfig> getDeserializer() {
        return SESSION_CONFIG_SERIALIZER;
    }
}
