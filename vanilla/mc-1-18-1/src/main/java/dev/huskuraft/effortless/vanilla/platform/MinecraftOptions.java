package dev.huskuraft.effortless.vanilla.platform;

import java.util.Arrays;

import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.platform.Options;
import dev.huskuraft.effortless.vanilla.input.MinecraftKeyBinding;

public record MinecraftOptions(net.minecraft.client.Options referenceValue) implements Options {

    @Override
    public KeyBinding keyUp() {
        return new MinecraftKeyBinding(referenceValue.keyUp);
    }

    @Override
    public KeyBinding keyLeft() {
        return new MinecraftKeyBinding(referenceValue.keyLeft);
    }

    @Override
    public KeyBinding keyDown() {
        return new MinecraftKeyBinding(referenceValue.keyDown);
    }

    @Override
    public KeyBinding keyRight() {
        return new MinecraftKeyBinding(referenceValue.keyRight);
    }

    @Override
    public KeyBinding keyJump() {
        return new MinecraftKeyBinding(referenceValue.keyJump);
    }

    @Override
    public KeyBinding keyShift() {
        return new MinecraftKeyBinding(referenceValue.keyShift);
    }

    @Override
    public KeyBinding keySprint() {
        return new MinecraftKeyBinding(referenceValue.keySprint);
    }

    @Override
    public KeyBinding keyInventory() {
        return new MinecraftKeyBinding(referenceValue.keyInventory);
    }

    @Override
    public KeyBinding keySwapOffhand() {
        return new MinecraftKeyBinding(referenceValue.keySwapOffhand);
    }

    @Override
    public KeyBinding keyDrop() {
        return new MinecraftKeyBinding(referenceValue.keyDrop);
    }

    @Override
    public KeyBinding keyUse() {
        return new MinecraftKeyBinding(referenceValue.keyUse);
    }

    @Override
    public KeyBinding keyAttack() {
        return new MinecraftKeyBinding(referenceValue.keyAttack);
    }

    @Override
    public KeyBinding keyPickItem() {
        return new MinecraftKeyBinding(referenceValue.keyPickItem);
    }

    @Override
    public KeyBinding keyChat() {
        return new MinecraftKeyBinding(referenceValue.keyChat);
    }

    @Override
    public KeyBinding keyPlayerList() {
        return new MinecraftKeyBinding(referenceValue.keyPlayerList);
    }

    @Override
    public KeyBinding keyCommand() {
        return new MinecraftKeyBinding(referenceValue.keyCommand);
    }

    @Override
    public KeyBinding keySocialInteractions() {
        return new MinecraftKeyBinding(referenceValue.keySocialInteractions);
    }

    @Override
    public KeyBinding keyScreenshot() {
        return new MinecraftKeyBinding(referenceValue.keyScreenshot);
    }

    @Override
    public KeyBinding keyTogglePerspective() {
        return new MinecraftKeyBinding(referenceValue.keyTogglePerspective);
    }

    @Override
    public KeyBinding keySmoothCamera() {
        return new MinecraftKeyBinding(referenceValue.keySmoothCamera);
    }

    @Override
    public KeyBinding keyFullscreen() {
        return new MinecraftKeyBinding(referenceValue.keyFullscreen);
    }

    @Override
    public KeyBinding keySpectatorOutlines() {
        return new MinecraftKeyBinding(referenceValue.keySpectatorOutlines);
    }

    @Override
    public KeyBinding keyAdvancements() {
        return new MinecraftKeyBinding(referenceValue.keyAdvancements);
    }

    @Override
    public KeyBinding keySaveHotbarActivator() {
        return new MinecraftKeyBinding(referenceValue.keySaveHotbarActivator);
    }

    @Override
    public KeyBinding keyLoadHotbarActivator() {
        return new MinecraftKeyBinding(referenceValue.keyLoadHotbarActivator);
    }

    @Override
    public KeyBinding[] keyHotbarSlots() {
        return Arrays.stream(referenceValue.keyHotbarSlots).map(MinecraftKeyBinding::new).toArray(KeyBinding[]::new);
    }

    @Override
    public int maxRenderDistance() {
        return referenceValue.renderDistance().get();
    }
}
