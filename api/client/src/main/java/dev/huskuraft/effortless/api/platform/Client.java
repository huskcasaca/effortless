package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.renderer.Camera;
import dev.huskuraft.effortless.api.renderer.Window;

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

    void playButtonClickSound();

    Resource getResource(ResourceLocation location);

}
