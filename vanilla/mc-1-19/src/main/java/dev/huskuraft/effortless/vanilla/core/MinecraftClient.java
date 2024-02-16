package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.renderer.Camera;
import dev.huskuraft.effortless.api.renderer.Window;
import dev.huskuraft.effortless.api.sound.SoundManager;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftCamera;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftWindow;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSoundManager;
import net.minecraft.client.Minecraft;

public class MinecraftClient implements Client {

    private final Minecraft reference;

    public MinecraftClient(Minecraft reference) {
        this.reference = reference;
    }

    @Override
    public Minecraft referenceValue() {
        return reference;
    }

    @Override
    public Window getWindow() {
        return new MinecraftWindow(reference.getWindow());
    }

    @Override
    public Camera getCamera() {
        return new MinecraftCamera(reference.gameRenderer.getMainCamera());
    }

    @Override
    public Screen getPanel() {
        if (reference.screen == null) {
            return null;
        }
        if (reference.screen instanceof MinecraftProxyScreen proxyScreen) {
            return proxyScreen.getProxy();
        }
        return new MinecraftScreen(reference.screen);
    }

    @Override
    public void setPanel(Screen screen) {
        if (screen == null) {
            reference.setScreen(null);
            return;
        }
        if (screen instanceof MinecraftScreen minecraftScreen) {
            reference.setScreen(minecraftScreen.getReference());
            return;
        }
        reference.setScreen(new MinecraftProxyScreen(screen));

    }

    @Override
    public Player getPlayer() {
        if (reference.player == null) {
            return null;
        }
        return new MinecraftPlayer(reference.player);
    }

    @Override
    public Typeface getTypeface() {
        return new MinecraftTypeface(reference.font);
    }

    @Override
    public World getWorld() {
        return new MinecraftWorld(reference.level);
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public Interaction getLastInteraction() {
        return MinecraftConvertor.fromPlatformInteraction(reference.hitResult);
    }

    @Override
    public String getClipboard() {
        return reference.keyboardHandler.getClipboard();
    }

    @Override
    public void setClipboard(String content) {
        reference.keyboardHandler.setClipboard(content);
    }

    @Override
    public SoundManager getSoundManager() {
        return new MinecraftSoundManager(reference.getSoundManager());
    }

    @Override
    public Resource getResource(ResourceLocation location) {
        var resource = reference.getResourceManager().getResource(location.reference());
        return resource.map(value -> new MinecraftResource(value, location.reference())).orElse(null);
    }

    @Override
    public void sendChat(String chat) {
        reference.player.chat(chat);
    }

    @Override
    public void sendCommand(String command) {
        reference.player.command(command);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftClient client && reference.equals(client.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
