package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.PlatformUtils;

public interface Window extends PlatformReference {

    int getWidth();

    int getHeight();

    int getGuiScaledWidth();

    int getGuiScaledHeight();

    double getGuiScaledFactor();

    boolean isKeyDown(int key);

    boolean isMouseButtonDown(int button);

    default boolean isControlDown() {
        return switch (PlatformUtils.getOS()) {
            case MACOS -> isKeyDown(343) || isKeyDown(347);
            default -> isKeyDown(341) || isKeyDown(345);
        };
    }

    default boolean isShiftDown() {
        return isKeyDown(340) || isKeyDown(344);
    }

    default boolean isAltDown() {
        return isKeyDown(342) || isKeyDown(346);
    }

    default boolean isCut(int key) {
        return key == 88 && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isPaste(int key) {
        return key == 86 && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isCopy(int key) {
        return key == 67 && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isSelectAll(int key) {
        return key == 65 && isControlDown() && !isShiftDown() && !isAltDown();
    }

}
