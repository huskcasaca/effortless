package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.gui.screens.Screen;

public class MinecraftScreen extends AbstractScreen {

    private final Screen reference;

    MinecraftScreen(Screen reference) {
        super(null, Text.empty());
        this.reference = reference;
    }

    public Screen getReference() {
        return reference;
    }

    @Override
    public Text getScreenTitle() {
        return MinecraftText.fromMinecraftText(reference.getTitle());
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isPauseGame() {
        return false;
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
