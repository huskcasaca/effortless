package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.api.renderer.Window;
import org.lwjgl.glfw.GLFW;

public class MinecraftWindow implements Window {

    private final com.mojang.blaze3d.platform.Window reference;

    public MinecraftWindow(com.mojang.blaze3d.platform.Window reference) {
        this.reference = reference;
    }

    @Override
    public com.mojang.blaze3d.platform.Window referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftWindow window && reference.equals(window.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public int getWidth() {
        return reference.getWidth();
    }

    @Override
    public int getHeight() {
        return reference.getHeight();
    }

    @Override
    public int getGuiScaledWidth() {
        return reference.getGuiScaledWidth();
    }

    @Override
    public int getGuiScaledHeight() {
        return reference.getGuiScaledHeight();
    }

    @Override
    public double getGuiScaledFactor() {
        return reference.getGuiScale();
    }

    @Override
    public boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(reference.getWindow(), key);
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        return GLFW.glfwGetMouseButton(reference.getWindow(), button) == 1;
    }
}
