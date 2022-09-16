package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.core.Interaction;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.Typeface;

public abstract class Client {

    public abstract Screen getPanel();

    public abstract void setPanel(Screen screen);

    public abstract Player getPlayer();

    public abstract Typeface getTypeface();

    public abstract World getWorld();

    public abstract boolean isLoaded();

    public abstract Interaction getLastInteraction();

    public abstract boolean hasControlDown();

    public abstract boolean hasShiftDown();

    public abstract boolean hasAltDown();

    public abstract boolean isCut(int key);

    public abstract boolean isPaste(int key);

    public abstract boolean isCopy(int key);

    public abstract boolean isSelectAll(int key);

    public abstract boolean isKeyDown(int key);

    public abstract boolean isMouseButtonDown(int button);

    public abstract String getClipboard();

    public abstract void setClipboard(String content);

    public abstract void playButtonClickSound();

}
