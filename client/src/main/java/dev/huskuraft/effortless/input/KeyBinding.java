package dev.huskuraft.effortless.input;

public abstract class KeyBinding {

    public abstract String getName();

    public abstract String getCategory();

    public abstract int getDefaultKey();

    public abstract boolean consumeClick();

    public abstract boolean isDown();

    public abstract boolean isKeyDown();

    public abstract String getBoundKey();

    public abstract int getBoundCode();

}
