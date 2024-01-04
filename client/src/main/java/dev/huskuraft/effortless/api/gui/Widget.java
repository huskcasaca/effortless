package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.core.Tickable;

public interface Widget extends Renderable, Tickable, InputHandler {

    void onCreate();

    void onLoad();

    void onReload();

    void onDestroy();

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isActive();

    void setActive(boolean active);

    boolean isHovered();

    void setHovered(boolean hovered);

    int getX();

    void setX(int x);

    void moveX(int x);

    int getY();

    void setY(int y);

    void moveY(int x);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

}

