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

    public KeyMapping getRef() {
        return keyMapping;
    }

    @Override
    public String getName() {
        return getRef().getName();
    }

    @Override
    public String getCategory() {
        return getRef().getCategory();
    }

    @Override
    public int getDefaultKey() {
        return getRef().getDefaultKey().getValue();
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
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), getRef().key.getValue());
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
