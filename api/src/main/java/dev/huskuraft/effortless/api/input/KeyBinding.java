package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface KeyBinding extends PlatformReference {

    static KeyBinding of(String name, String category, int code) {
        return ClientContentFactory.getInstance().newKeyBinding(name, category, code);
    }

    static KeyBinding of(int code) {
        return ClientContentFactory.getInstance().newKeyBinding(code);
    }

    String getName();

    String getCategory();

    int getDefaultKey();

    boolean consumeClick();

    boolean isDown();

    boolean isKeyDown();

    String getBoundKey();

    int getBoundCode();

}
