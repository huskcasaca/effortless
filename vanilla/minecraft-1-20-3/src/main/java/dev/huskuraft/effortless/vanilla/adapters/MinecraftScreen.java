package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.gui.screens.Screen;

class MinecraftScreen extends AbstractScreen {

    private final Screen screen;

    MinecraftScreen(Screen screen) {
        super(null, Text.empty());
        this.screen = screen;
    }

    public Screen getRef() {
        return screen;
    }

    @Override
    public Text getScreenTitle() {
        return MinecraftClientAdapter.adapt(getRef().getTitle());
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
