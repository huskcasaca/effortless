package dev.huskuraft.effortless.api.input;

import org.lwjgl.glfw.GLFW;

public record InputKey(int key, int scanCode, int action, int modifiers) {

    public boolean isShiftPressed() {
        return (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
    }

    public boolean isControlPressed() {
        return (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
    }

    public boolean isAltPressed() {
        return (modifiers & GLFW.GLFW_MOD_ALT) != 0;
    }

    public boolean isSuperPressed() {
        return (modifiers & GLFW.GLFW_MOD_SUPER) != 0;
    }

    public boolean isCapsLockPressed() {
        return (modifiers & GLFW.GLFW_MOD_CAPS_LOCK) != 0;
    }

    public boolean isNumLockPressed() {
        return (modifiers & GLFW.GLFW_MOD_NUM_LOCK) != 0;
    }

    public enum Type {
        PRESS(GLFW.GLFW_PRESS),
        RELEASE(GLFW.GLFW_RELEASE),
        REPEAT(GLFW.GLFW_REPEAT),
        ;

        public final int value;

        Type(int value) {
            this.value = value;
        }
    }

}
