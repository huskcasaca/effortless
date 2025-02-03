package dev.huskuraft.effortless.vanilla.input;

import dev.huskuraft.effortless.api.input.Key;
import dev.huskuraft.effortless.api.input.KeyBinding;
import net.minecraft.client.KeyMapping;

public record MinecraftKeyBinding(
        KeyMapping refs
) implements KeyBinding {

    @Override
    public String getName() {
        return refs.getName();
    }

    @Override
    public String getCategory() {
        return refs.getCategory();
    }

    @Override
    public Key getDefaultKey() {
        return new MinecraftKey(refs.getDefaultKey());
    }

    @Override
    public Key getKey() {
        return new MinecraftKey(refs.key);
    }

    @Override
    public boolean consumeClick() {
        return refs.consumeClick();
    }

    @Override
    public boolean isDown() {
        return refs.isDown();
    }

}
