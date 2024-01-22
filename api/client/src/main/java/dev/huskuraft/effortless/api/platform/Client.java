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

    default boolean isControlDown() {
        return getWindow().hasControlDown();
    }

    default boolean isShiftDown() {
        return getWindow().hasShiftDown();
    }

    default boolean isAltDown() {
        return getWindow().hasAltDown();
    }

    default boolean isCut(int key) {
        return getWindow().isCut(key);
    }

    default boolean isPaste(int key) {
        return getWindow().isPaste(key);
    }

    default boolean isCopy(int key) {
        return getWindow().isCopy(key);
    }

    default boolean isSelectAll(int key) {
        return getWindow().isSelectAll(key);
    }

    String getClipboard();

    void setClipboard(String content);

    void playButtonClickSound();

    Resource getResource(ResourceLocation location);

}
