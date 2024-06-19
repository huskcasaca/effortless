package dev.huskuraft.effortless.vanilla.platform;

import java.util.Arrays;

import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.platform.Options;
import dev.huskuraft.effortless.vanilla.input.MinecraftKeyBinding;

public record MinecraftOptions(net.minecraft.client.Options refs) implements Options {

    @Override
    public KeyBinding keyUp() {
        return new MinecraftKeyBinding(refs.keyUp);
    }

    @Override
    public KeyBinding keyLeft() {
        return new MinecraftKeyBinding(refs.keyLeft);
    }

    @Override
    public KeyBinding keyDown() {
        return new MinecraftKeyBinding(refs.keyDown);
    }

    @Override
    public KeyBinding keyRight() {
        return new MinecraftKeyBinding(refs.keyRight);
    }

    @Override
    public KeyBinding keyJump() {
        return new MinecraftKeyBinding(refs.keyJump);
    }

    @Override
    public KeyBinding keyShift() {
        return new MinecraftKeyBinding(refs.keyShift);
    }

    @Override
    public KeyBinding keySprint() {
        return new MinecraftKeyBinding(refs.keySprint);
    }

    @Override
    public KeyBinding keyInventory() {
        return new MinecraftKeyBinding(refs.keyInventory);
    }

    @Override
    public KeyBinding keySwapOffhand() {
        return new MinecraftKeyBinding(refs.keySwapOffhand);
    }

    @Override
    public KeyBinding keyDrop() {
        return new MinecraftKeyBinding(refs.keyDrop);
    }

    @Override
    public KeyBinding keyUse() {
        return new MinecraftKeyBinding(refs.keyUse);
    }

    @Override
    public KeyBinding keyAttack() {
        return new MinecraftKeyBinding(refs.keyAttack);
    }

    @Override
    public KeyBinding keyPickItem() {
        return new MinecraftKeyBinding(refs.keyPickItem);
    }

    @Override
    public KeyBinding keyChat() {
        return new MinecraftKeyBinding(refs.keyChat);
    }

    @Override
    public KeyBinding keyPlayerList() {
        return new MinecraftKeyBinding(refs.keyPlayerList);
    }

    @Override
    public KeyBinding keyCommand() {
        return new MinecraftKeyBinding(refs.keyCommand);
    }

    @Override
    public KeyBinding keySocialInteractions() {
        return new MinecraftKeyBinding(refs.keySocialInteractions);
    }

    @Override
    public KeyBinding keyScreenshot() {
        return new MinecraftKeyBinding(refs.keyScreenshot);
    }

    @Override
    public KeyBinding keyTogglePerspective() {
        return new MinecraftKeyBinding(refs.keyTogglePerspective);
    }

    @Override
    public KeyBinding keySmoothCamera() {
        return new MinecraftKeyBinding(refs.keySmoothCamera);
    }

    @Override
    public KeyBinding keyFullscreen() {
        return new MinecraftKeyBinding(refs.keyFullscreen);
    }

    @Override
    public KeyBinding keySpectatorOutlines() {
        return new MinecraftKeyBinding(refs.keySpectatorOutlines);
    }

    @Override
    public KeyBinding keyAdvancements() {
        return new MinecraftKeyBinding(refs.keyAdvancements);
    }

    @Override
    public KeyBinding keySaveHotbarActivator() {
        return new MinecraftKeyBinding(refs.keySaveHotbarActivator);
    }

    @Override
    public KeyBinding keyLoadHotbarActivator() {
        return new MinecraftKeyBinding(refs.keyLoadHotbarActivator);
    }

    @Override
    public KeyBinding[] keyHotbarSlots() {
        return Arrays.stream(refs.keyHotbarSlots).map(MinecraftKeyBinding::new).toArray(KeyBinding[]::new);
    }

    @Override
    public int renderDistance() {
        return refs.renderDistance;
    }
}
