package dev.huskuraft.effortless.api.platform;

public interface Entrance {

    String getId();

    default Platform getPlatform() {
        return Platform.INSTANCE;
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

