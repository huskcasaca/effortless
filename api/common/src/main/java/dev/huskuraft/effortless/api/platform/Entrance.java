package dev.huskuraft.effortless.api.platform;

import java.nio.file.Path;

public interface Entrance {

    String getId();

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

    static Entrance getInstance() {
        return Instance.get();
    }

    class Instance {
        private Instance() {
        }
        private static Entrance instance;
        public static Entrance get() {
            return Instance.instance;
        }
        public static void set(Entrance instance) {
            Instance.instance = instance;
        }
    }

}

