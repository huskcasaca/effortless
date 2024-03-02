package dev.huskuraft.effortless.api.renderer;

import org.lwjgl.glfw.GLFW;

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
        if (PlatformUtils.isMacOS()) {
            return isKeyDown(GLFW.GLFW_KEY_LEFT_SUPER) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SUPER);
        } else {
            return isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
        }
    }

    default boolean isShiftDown() {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    default boolean isAltDown() {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_ALT);
    }

    default boolean isCut(int key) {
        return key == GLFW.GLFW_KEY_X && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isPaste(int key) {
        return key == GLFW.GLFW_KEY_V && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isCopy(int key) {
        return key == GLFW.GLFW_KEY_C && isControlDown() && !isShiftDown() && !isAltDown();
    }

    default boolean isSelectAll(int key) {
        return key == GLFW.GLFW_KEY_A && isControlDown() && !isShiftDown() && !isAltDown();
    }

}
