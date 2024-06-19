package dev.huskuraft.effortless.vanilla.platform;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.Options;
import dev.huskuraft.effortless.api.platform.ParticleEngine;
import dev.huskuraft.effortless.api.renderer.Camera;
import dev.huskuraft.effortless.api.renderer.Window;
import dev.huskuraft.effortless.api.sound.SoundManager;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayerInfo;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import dev.huskuraft.effortless.vanilla.gui.MinecraftProxyScreen;
import dev.huskuraft.effortless.vanilla.gui.MinecraftScreen;
import dev.huskuraft.effortless.vanilla.gui.MinecraftTypeface;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftCamera;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftWindow;
import dev.huskuraft.effortless.vanilla.sound.MinecraftParticleEngine;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSoundManager;
import net.minecraft.client.Minecraft;

public record MinecraftClient(
        Minecraft refs
) implements Client {

    @Override
    public Window getWindow() {
        return new MinecraftWindow(refs.getWindow());
    }

    @Override
    public Camera getCamera() {
        return new MinecraftCamera(refs.gameRenderer.getMainCamera());
    }

    @Override
    public Screen getPanel() {
        if (refs.screen == null) {
            return null;
        }
        if (refs.screen instanceof MinecraftProxyScreen proxyScreen) {
            return proxyScreen.getProxy();
        }
        return new MinecraftScreen(refs.screen);
    }

    @Override
    public void setPanel(Screen screen) {
        if (screen == null) {
            refs.setScreen(null);
            return;
        }
        if (screen instanceof MinecraftScreen minecraftScreen) {
            refs.setScreen(minecraftScreen.refs());
            return;
        }
        refs.setScreen(new MinecraftProxyScreen(screen));

    }

    @Override
    public Player getPlayer() {
        return MinecraftPlayer.ofNullable(refs.player);
    }

    @Override
    public List<PlayerInfo> getOnlinePlayers() {
        if (refs.getConnection() == null) return List.of();
        return refs.getConnection().getOnlinePlayers().stream().map(MinecraftPlayerInfo::new).collect(Collectors.toList());
    }

    @Override
    public Typeface getTypeface() {
        return new MinecraftTypeface(refs.font);
    }

    @Override
    public World getWorld() {
        return MinecraftWorld.ofNullable(refs.level);
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public Interaction getLastInteraction() {
        return MinecraftConvertor.fromPlatformInteraction(refs.hitResult);
    }

    @Override
    public String getClipboard() {
        return refs.keyboardHandler.getClipboard();
    }

    @Override
    public void setClipboard(String content) {
        refs.keyboardHandler.setClipboard(content);
    }

    @Override
    public SoundManager getSoundManager() {
        return new MinecraftSoundManager(refs.getSoundManager());
    }

    @Override
    public void sendChat(String chat) {
        refs.player.chatSigned(chat, null);
    }

    @Override
    public void sendCommand(String command) {
        refs.player.commandSigned(command, null);
    }

    @Override
    public void execute(Runnable runnable) {
        refs.execute(runnable);
    }

    @Override
    public Options getOptions() {
        return new MinecraftOptions(refs.options);
    }

    @Override
    public ParticleEngine getParticleEngine() {
        return new MinecraftParticleEngine(refs.particleEngine);
    }

    @Override
    public boolean isLocalServer() {
        return refs.isLocalServer();
    }

    @Override
    public boolean hasSinglePlayerServer() {
        return refs.hasSingleplayerServer();
    }
}
