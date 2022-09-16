package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.input.KeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

class MinecraftKeyBinding extends KeyBinding {

    private final KeyMapping keyMapping;

    MinecraftKeyBinding(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }

    public KeyMapping getRef() {
        return keyMapping;
    }

    @Override
    public boolean consumeClick() {
        return getRef().consumeClick();
    }

    @Override
    public boolean isDown() {
        return getRef().isDown();
    }

    @Override
    public boolean isKeyDown() {
        return isKeyDown(getRef().key.getValue());
    }

    @Override
    public String getBoundKey() {
        return getRef().getTranslatedKeyMessage().getString().toUpperCase();
    }

    @Override
    public int getBoundCode() {
        return getRef().key.getValue();
    }

}
