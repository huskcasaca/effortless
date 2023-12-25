package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.config.ConfigManager;
import dev.huskuraft.effortless.events.EventsRegistry;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.platform.GamePlatform;

import java.nio.file.Path;
import java.util.logging.Logger;

public abstract class Entrance {

    private static final Logger LOGGER = Logger.getLogger("Effortless");

    public abstract Channel getChannel();

    public abstract EventsRegistry getEventRegistry();

    public abstract StructureBuilder getStructureBuilder();

    public abstract ConfigManager getConfigManager();

    public abstract String getId();

    public abstract String getLoaderName();

    public abstract String getLoaderVersion();

    public abstract String getGameVersion();

    public abstract Path getGameDir();

    public abstract Path getConfigDir();

    public abstract GamePlatform getGamePlatform();

    public abstract Environment getEnvironment();

    public abstract boolean isDevelopment();

    public final void info(String msg) {
        if (isDevelopment()) {
            LOGGER.info(msg);
        }
    }

    public enum Environment {
        CLIENT,
        SERVER
    }

}

