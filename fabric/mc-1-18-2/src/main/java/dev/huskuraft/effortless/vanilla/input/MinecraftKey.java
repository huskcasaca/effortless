package dev.huskuraft.effortless.vanilla.input;

import com.mojang.blaze3d.platform.InputConstants;

import dev.huskuraft.effortless.api.input.Key;

public record MinecraftKey(
        InputConstants.Key refs
) implements Key {

    @Override
    public String getName() {
        return refs.getName();
    }

    @Override
    public int getValue() {
        return refs.getValue();
    }
}
