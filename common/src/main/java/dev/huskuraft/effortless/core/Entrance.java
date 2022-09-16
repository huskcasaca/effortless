package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.config.ConfigManager;
import dev.huskuraft.effortless.config.ConfigReader;
import dev.huskuraft.effortless.config.ConfigWriter;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.events.EventsRegistry;
import dev.huskuraft.effortless.networking.Channel;

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

    public abstract ConfigReader getConfigReader();

    public abstract ConfigWriter getConfigWriter();

    public abstract ContentCreator getContentCreator();

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

