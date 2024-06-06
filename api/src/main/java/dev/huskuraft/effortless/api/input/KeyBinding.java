package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface KeyBinding extends PlatformReference {

    static KeyBinding of(String name, String category, int code) {
        return ClientContentFactory.getInstance().newKeyBinding(name, category, code);
    }

    String getName();

    String getCategory();

    Key getDefaultKey();

    Key getKey();

    boolean consumeClick();

    boolean isDown();

    default boolean isKeyDown() {
        return getKey().isDown();
    }

}
