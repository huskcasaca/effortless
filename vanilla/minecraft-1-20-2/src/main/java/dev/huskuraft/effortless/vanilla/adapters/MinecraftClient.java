package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.core.Interaction;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.platform.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class MinecraftClient extends Client {

    private final Minecraft minecraft;

    public MinecraftClient(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Minecraft getRef() {
        return minecraft;
    }

    @Override
    public Screen getPanel() {
        if (getRef().screen == null) {
            return null;
        }
        if (getRef().screen instanceof MinecraftProxyScreen proxyScreen) {
            return proxyScreen.getProxy();
        }
        return new MinecraftScreen(getRef().screen);
    }

    @Override
    public void setPanel(Screen screen) {
        if (screen == null) {
            getRef().setScreen(null);
            return;
        }
        if (screen instanceof MinecraftScreen screen1) {
            getRef().setScreen(screen1.getRef());
            return;
        }
        getRef().setScreen(new MinecraftProxyScreen(screen));

    }

    @Override
    public Player getPlayer() {
        if (getRef().player == null) {
            return null;
        }
        return new MinecraftClientPlayer(getRef().player);
    }

    @Override
    public Typeface getTypeface() {
        return MinecraftTypeface.fromMinecraftTypeface(getRef().font);
    }

    @Override
    public World getWorld() {
        return new MinecraftWorld(getRef().level);
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public Interaction getLastInteraction() {
        return MinecraftBasicTypes.fromMinecraftInteraction(getRef().hitResult);
    }

    @Override
    public boolean hasControlDown() {
        return net.minecraft.client.gui.screens.Screen.hasControlDown();
    }

    @Override
    public boolean hasShiftDown() {
        return net.minecraft.client.gui.screens.Screen.hasShiftDown();
    }

    @Override
    public boolean hasAltDown() {
        return net.minecraft.client.gui.screens.Screen.hasAltDown();
    }

    @Override
    public boolean isCut(int key) {
        return net.minecraft.client.gui.screens.Screen.isCut(key);
    }

    @Override
    public boolean isPaste(int key) {
        return net.minecraft.client.gui.screens.Screen.isPaste(key);
    }

    @Override
    public boolean isCopy(int key) {
        return net.minecraft.client.gui.screens.Screen.isCopy(key);
    }

    @Override
    public boolean isSelectAll(int key) {
        return net.minecraft.client.gui.screens.Screen.isSelectAll(key);
    }

    @Override
    public boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(getRef()
                .getWindow()
                .getWindow(), key);
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        return GLFW.glfwGetMouseButton(getRef()
                .getWindow()
                .getWindow(), button) == 1;
    }

    @Override
    public String getClipboard() {
        return getRef().keyboardHandler.getClipboard();
    }

    @Override
    public void setClipboard(String content) {
        getRef().keyboardHandler.setClipboard(content);
    }

    @Override
    public void playButtonClickSound() {
        getRef().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
