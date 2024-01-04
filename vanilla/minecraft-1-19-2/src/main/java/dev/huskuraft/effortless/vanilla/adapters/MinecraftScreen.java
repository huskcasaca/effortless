package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.text.Text;
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
        return reference.isPauseScreen();
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftScreen screen && reference.equals(screen.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
