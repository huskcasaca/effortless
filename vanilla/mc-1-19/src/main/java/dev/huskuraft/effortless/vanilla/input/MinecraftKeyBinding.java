package dev.huskuraft.effortless.vanilla.input;

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
    public int getDefaultKey() {
        return refs.getDefaultKey().getValue();
    }

    @Override
    public boolean consumeClick() {
        return refs.consumeClick();
    }

    @Override
    public boolean isDown() {
        return refs.isDown();
    }

    @Override
    public int getBoundCode() {
        return refs.key.getValue();
    }

}
