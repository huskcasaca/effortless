package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface KeyBinding extends PlatformReference {

    String getName();

    String getCategory();

    int getDefaultKey();

    boolean consumeClick();

    boolean isDown();

    boolean isKeyDown();

    String getBoundKey();

    int getBoundCode();

    static KeyBinding of(String name, String category, KeyCodes key) {
        return ClientContentFactory.getInstance().newKeyBinding(name, category, key);
    }

}
