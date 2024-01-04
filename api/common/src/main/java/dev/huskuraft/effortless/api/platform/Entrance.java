package dev.huskuraft.effortless.api.platform;

import java.nio.file.Path;
import java.util.logging.Logger;

public abstract class Entrance {

    private static final Logger LOGGER = Logger.getLogger("Effortless");

    public abstract String getId();

    public abstract String getLoaderName();

    public abstract String getLoaderVersion();

    public abstract String getGameVersion();

    public abstract Path getGameDir();

    public abstract Path getConfigDir();

    public abstract Platform getPlatform();

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

    protected static Entrance instance;

    public static Entrance getInstance() {
        return instance;
    }
}

