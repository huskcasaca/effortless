package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.renderer.Camera;
import dev.huskuraft.effortless.api.renderer.Window;
import dev.huskuraft.effortless.api.sound.SoundManager;

public interface Client extends PlatformReference {

    Window getWindow();

    Camera getCamera();

    Screen getPanel();

    void setPanel(Screen screen);

    Player getPlayer();

    Typeface getTypeface();

    World getWorld();

    boolean isLoaded();

    Interaction getLastInteraction();

    String getClipboard();

    void setClipboard(String content);

    SoundManager getSoundManager();

    Resource getResource(ResourceLocation location);

    void sendChat(String chat);

    void sendCommand(String command);

}
