package dev.huskuraft.effortless.api.gui;

public interface Widget extends Renderable, InputHandler {

    void tick();

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

    default int getTop() {
        return getY();
    }

    default int getBottom() {
        return getY() + getHeight();
    }

    void setHeight(int height);

}

