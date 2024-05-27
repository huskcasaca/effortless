package dev.huskuraft.effortless.api.lang;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformLoader;
import dev.huskuraft.effortless.api.text.Text;

public interface Lang {

    static Lang getInstance() {
        return PlatformLoader.getSingleton();
    }

    static Text translate(String key) {
        return Text.translate(Entrance.getInstance().getId() + "." + key);
    }

    static Text translate(String key, Object... args) {
        return Text.translate(Entrance.getInstance().getId() + "." + key, args);
    }

    static Text translateKeyDesc(String key) {
        return Text.translate(asKeyDesc(key));
    }


    static String asKey(String key) {
        return "%s.%s".formatted(Entrance.getInstance().getId(), key);
    }


    static String asKeyDesc(String key) {
        return "key.%s.%s.desc".formatted(Entrance.getInstance().getId(), key);
    }

    static boolean hasKey(String key) {
        return getInstance().has(key);
    }

    String getOrDefault(String id);

    boolean has(String id);

    boolean isDefaultRightToLeft();

}
