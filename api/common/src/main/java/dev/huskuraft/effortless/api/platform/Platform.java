package dev.huskuraft.effortless.api.platform;

import java.nio.file.Path;

public interface Platform {

    String getLoaderName();

    String getLoaderVersion();

    String getGameVersion();

    Path getGameDir();

    Path getConfigDir();

    ContentFactory getContentFactory();

    Environment getEnvironment();

    boolean isDevelopment();

    enum Environment {
        CLIENT,
        SERVER
    }

}
