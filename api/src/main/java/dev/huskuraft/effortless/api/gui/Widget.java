package dev.huskuraft.effortless.api.gui;

public interface Widget extends Renderable, InputHandler {

    int UNSPECIFIC_SIZE = 0;

    void onTick();

    void onAnimateTick(float partialTick);

    void onCreate();

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

    default int getTop() {
        return getY();
    }

    default int getBottom() {
        return getY() + getHeight();
    }

    default int getLeft() {
        return getX();
    }

    default int getRight() {
        return getX() + getWidth();
    }

    default int getCenterY() {
        return getY() + getHeight() / 2;
    }

    default int getCenterX() {
        return getX() + getWidth() / 2;
    }

    Widget getParent();

    default void recreate() {
        onDestroy();
        onCreate();
        onReload();
    }

}

