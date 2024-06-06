package dev.huskuraft.effortless.api.lang;

import dev.huskuraft.effortless.api.platform.PlatformLoader;

public interface Lang {

    static Lang getInstance() {
        return PlatformLoader.getSingleton();
    }

    static String asKey(String namespace, String key) {
        return "%s.%s".formatted(namespace, key);
    }

    static String getKeyDesc(String namespace, String key) {
        return "key.%s.%s".formatted(namespace, key);
    }

    static boolean hasKey(String key) {
        return getInstance().has(key);
    }

    String getOrDefault(String id);

    boolean has(String id);

    boolean isDefaultRightToLeft();

}
