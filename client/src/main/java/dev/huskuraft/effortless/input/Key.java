package dev.huskuraft.effortless.input;

public interface Key {

    boolean consumeClick();

    String getName();

    String getCategory();

    void bindKeyMapping(KeyBinding keyBinding);

    KeyCodes getDefaultKey();

    boolean isDown();

    boolean isKeyDown();

    String getBoundKey();

    int getBoundCode();
}
