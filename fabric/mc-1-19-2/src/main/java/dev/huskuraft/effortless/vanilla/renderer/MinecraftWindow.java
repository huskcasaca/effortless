package dev.huskuraft.effortless.vanilla.renderer;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import dev.huskuraft.effortless.api.renderer.Window;

public record MinecraftWindow(
        com.mojang.blaze3d.platform.Window refs
) implements Window {

    @Override
    public int hashCode() {
        return refs.hashCode();
    }

    @Override
    public int getWidth() {
        return refs.getWidth();
    }

    @Override
    public int getHeight() {
        return refs.getHeight();
    }

    @Override
    public int getGuiScaledWidth() {
        return refs.getGuiScaledWidth();
    }

    @Override
    public int getGuiScaledHeight() {
        return refs.getGuiScaledHeight();
    }

    @Override
    public double getGuiScaledFactor() {
        return refs.getGuiScale();
    }

    @Override
    public boolean isKeyDown(int key) {
        try {
            return InputConstants.isKeyDown(refs.getWindow(), key);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        try {
            return GLFW.glfwGetMouseButton(refs.getWindow(), button) == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
