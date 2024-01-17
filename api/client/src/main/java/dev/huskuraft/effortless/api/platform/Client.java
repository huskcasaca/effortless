package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Typeface;

public interface Client extends PlatformReference {

    Screen getPanel();

    void setPanel(Screen screen);

    Player getPlayer();

    Typeface getTypeface();

    World getWorld();

    boolean isLoaded();

    Interaction getLastInteraction();

    boolean hasControlDown();

    boolean hasShiftDown();

    boolean hasAltDown();

    boolean isCut(int key);

    boolean isPaste(int key);

    boolean isCopy(int key);

    boolean isSelectAll(int key);

    boolean isKeyDown(int key);

    boolean isMouseButtonDown(int button);

    String getClipboard();

    void setClipboard(String content);

    void playButtonClickSound();

}
