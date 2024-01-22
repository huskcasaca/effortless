package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Window extends PlatformReference {

    int getWidth();

    int getHeight();

    int getGuiScaledWidth();

    int getGuiScaledHeight();

    double getGuiScaledFactor();

    boolean isKeyDown(int key);

    boolean isMouseButtonDown(int button);

    default boolean hasControlDown() {
        if (true) { // ON_OSX
            return isKeyDown(343) || isKeyDown(347);
        } else {
            return isKeyDown(341) || isKeyDown(345);
        }
    }

    default boolean hasShiftDown() {
        return isKeyDown(340) || isKeyDown(344);
    }

    default boolean hasAltDown() {
        return isKeyDown(342) || isKeyDown(346);
    }

    default boolean isCut(int key) {
        return key == 88 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    default boolean isPaste(int key) {
        return key == 86 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    default boolean isCopy(int key) {
        return key == 67 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    default boolean isSelectAll(int key) {
        return key == 65 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

}
