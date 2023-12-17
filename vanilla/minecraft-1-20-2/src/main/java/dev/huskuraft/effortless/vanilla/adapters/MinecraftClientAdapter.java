package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyCodes;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.renderer.RenderType;
import dev.huskuraft.effortless.renderer.Renderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class MinecraftClientAdapter extends MinecraftAdapter {

    public static Player adapt(net.minecraft.world.entity.player.Player player) {
        if (player == null) {
            return null;
        }
        return new MinecraftClientPlayer(player);
    }

    public static net.minecraft.world.entity.player.Player adapt(Player player) {
        if (player == null) {
            return null;
        }
        return ((MinecraftClientPlayer) player).getRef();
    }

    public static Client adapt(Minecraft client) {
        if (client == null) {
            return null;
        }
        return new MinecraftClient(client);
    }

    public static Screen adapt(net.minecraft.client.gui.screens.Screen screen) {
        if (screen == null) {
            return null;
        }
        if (screen instanceof MinecraftProxyScreen proxyScreen) {
            return proxyScreen.getProxy();
        }
        return new MinecraftScreen(screen);
    }

    public static net.minecraft.client.gui.screens.Screen adapt(Screen screen) {
        if (screen == null) {
            return null;
        }
        if (screen instanceof MinecraftScreen screen1) {
            return screen1.getRef();
        }
        return new MinecraftProxyScreen(screen);
    }

    public static Renderer adapt(GuiGraphics guiGraphics) {
        if (guiGraphics == null) {
            return null;
        }
        return new MinecraftRenderer(guiGraphics);
    }

    public static RenderType adapt(net.minecraft.client.renderer.RenderType renderType) {
        if (renderType == null) {
            return null;
        }
        return new MinecraftRenderType(renderType);
    }

    public static net.minecraft.client.renderer.RenderType adapt(RenderType renderType) {
        if (renderType == null) {
            return null;
        }
        return ((MinecraftRenderType) renderType).getRef();
    }

    public static Typeface adapt(Font font) {
        if (font == null) {
            return null;
        }
        return new MinecraftTypeface(font);
    }

    public static Font adapt(Typeface typeface) {
        if (typeface == null) {
            return null;
        }
        return ((MinecraftTypeface) typeface).getRef();
    }

    public static int adapt(KeyCodes keyCodes) {
        return switch (keyCodes) {
            case KEY_UNKNOWN -> InputConstants.UNKNOWN.getValue();
            case KEY_0 -> InputConstants.KEY_0;
            case KEY_1 -> InputConstants.KEY_1;
            case KEY_2 -> InputConstants.KEY_2;
            case KEY_3 -> InputConstants.KEY_3;
            case KEY_4 -> InputConstants.KEY_4;
            case KEY_5 -> InputConstants.KEY_5;
            case KEY_6 -> InputConstants.KEY_6;
            case KEY_7 -> InputConstants.KEY_7;
            case KEY_8 -> InputConstants.KEY_8;
            case KEY_9 -> InputConstants.KEY_9;
            case KEY_A -> InputConstants.KEY_A;
            case KEY_B -> InputConstants.KEY_B;
            case KEY_C -> InputConstants.KEY_C;
            case KEY_D -> InputConstants.KEY_D;
            case KEY_E -> InputConstants.KEY_E;
            case KEY_F -> InputConstants.KEY_F;
            case KEY_G -> InputConstants.KEY_G;
            case KEY_H -> InputConstants.KEY_H;
            case KEY_I -> InputConstants.KEY_I;
            case KEY_J -> InputConstants.KEY_J;
            case KEY_K -> InputConstants.KEY_K;
            case KEY_L -> InputConstants.KEY_L;
            case KEY_M -> InputConstants.KEY_M;
            case KEY_N -> InputConstants.KEY_N;
            case KEY_O -> InputConstants.KEY_O;
            case KEY_P -> InputConstants.KEY_P;
            case KEY_Q -> InputConstants.KEY_Q;
            case KEY_R -> InputConstants.KEY_R;
            case KEY_S -> InputConstants.KEY_S;
            case KEY_T -> InputConstants.KEY_T;
            case KEY_U -> InputConstants.KEY_U;
            case KEY_V -> InputConstants.KEY_V;
            case KEY_W -> InputConstants.KEY_W;
            case KEY_X -> InputConstants.KEY_X;
            case KEY_Y -> InputConstants.KEY_Y;
            case KEY_Z -> InputConstants.KEY_Z;
            case KEY_F1 -> InputConstants.KEY_F1;
            case KEY_F2 -> InputConstants.KEY_F2;
            case KEY_F3 -> InputConstants.KEY_F3;
            case KEY_F4 -> InputConstants.KEY_F4;
            case KEY_F5 -> InputConstants.KEY_F5;
            case KEY_F6 -> InputConstants.KEY_F6;
            case KEY_F7 -> InputConstants.KEY_F7;
            case KEY_F8 -> InputConstants.KEY_F8;
            case KEY_F9 -> InputConstants.KEY_F9;
            case KEY_F10 -> InputConstants.KEY_F10;
            case KEY_F11 -> InputConstants.KEY_F11;
            case KEY_F12 -> InputConstants.KEY_F12;
            case KEY_F13 -> InputConstants.KEY_F13;
            case KEY_F14 -> InputConstants.KEY_F14;
            case KEY_F15 -> InputConstants.KEY_F15;
            case KEY_F16 -> InputConstants.KEY_F16;
            case KEY_F17 -> InputConstants.KEY_F17;
            case KEY_F18 -> InputConstants.KEY_F18;
            case KEY_F19 -> InputConstants.KEY_F19;
            case KEY_F20 -> InputConstants.KEY_F20;
            case KEY_F21 -> InputConstants.KEY_F21;
            case KEY_F22 -> InputConstants.KEY_F22;
            case KEY_F23 -> InputConstants.KEY_F23;
            case KEY_F24 -> InputConstants.KEY_F24;
            case KEY_F25 -> InputConstants.KEY_F25;
            case KEY_NUMLOCK -> InputConstants.KEY_NUMLOCK;
            case KEY_NUMPAD0 -> InputConstants.KEY_NUMPAD0;
            case KEY_NUMPAD1 -> InputConstants.KEY_NUMPAD1;
            case KEY_NUMPAD2 -> InputConstants.KEY_NUMPAD2;
            case KEY_NUMPAD3 -> InputConstants.KEY_NUMPAD3;
            case KEY_NUMPAD4 -> InputConstants.KEY_NUMPAD4;
            case KEY_NUMPAD5 -> InputConstants.KEY_NUMPAD5;
            case KEY_NUMPAD6 -> InputConstants.KEY_NUMPAD6;
            case KEY_NUMPAD7 -> InputConstants.KEY_NUMPAD7;
            case KEY_NUMPAD8 -> InputConstants.KEY_NUMPAD8;
            case KEY_NUMPAD9 -> InputConstants.KEY_NUMPAD9;
            case KEY_NUMPADCOMMA -> InputConstants.KEY_NUMPADCOMMA;
            case KEY_NUMPADENTER -> InputConstants.KEY_NUMPADENTER;
            case KEY_NUMPADEQUALS -> InputConstants.KEY_NUMPADEQUALS;
            case KEY_DOWN -> InputConstants.KEY_DOWN;
            case KEY_LEFT -> InputConstants.KEY_LEFT;
            case KEY_RIGHT -> InputConstants.KEY_RIGHT;
            case KEY_UP -> InputConstants.KEY_UP;
            case KEY_ADD -> InputConstants.KEY_ADD;
            case KEY_APOSTROPHE -> InputConstants.KEY_APOSTROPHE;
            case KEY_BACKSLASH -> InputConstants.KEY_BACKSLASH;
            case KEY_COMMA -> InputConstants.KEY_COMMA;
            case KEY_EQUALS -> InputConstants.KEY_EQUALS;
            case KEY_GRAVE -> InputConstants.KEY_GRAVE;
            case KEY_LBRACKET -> InputConstants.KEY_LBRACKET;
            case KEY_MINUS -> InputConstants.KEY_MINUS;
            case KEY_MULTIPLY -> InputConstants.KEY_MULTIPLY;
            case KEY_PERIOD -> InputConstants.KEY_PERIOD;
            case KEY_RBRACKET -> InputConstants.KEY_RBRACKET;
            case KEY_SEMICOLON -> InputConstants.KEY_SEMICOLON;
            case KEY_SLASH -> InputConstants.KEY_SLASH;
            case KEY_SPACE -> InputConstants.KEY_SPACE;
            case KEY_TAB -> InputConstants.KEY_TAB;
            case KEY_LALT -> InputConstants.KEY_LALT;
            case KEY_LCONTROL -> InputConstants.KEY_LCONTROL;
            case KEY_LSHIFT -> InputConstants.KEY_LSHIFT;
            case KEY_LWIN -> InputConstants.KEY_LWIN;
            case KEY_RALT -> InputConstants.KEY_RALT;
            case KEY_RCONTROL -> InputConstants.KEY_RCONTROL;
            case KEY_RSHIFT -> InputConstants.KEY_RSHIFT;
            case KEY_RWIN -> InputConstants.KEY_RWIN;
            case KEY_RETURN -> InputConstants.KEY_RETURN;
            case KEY_ESCAPE -> InputConstants.KEY_ESCAPE;
            case KEY_BACKSPACE -> InputConstants.KEY_BACKSPACE;
            case KEY_DELETE -> InputConstants.KEY_DELETE;
            case KEY_END -> InputConstants.KEY_END;
            case KEY_HOME -> InputConstants.KEY_HOME;
            case KEY_INSERT -> InputConstants.KEY_INSERT;
            case KEY_PAGEDOWN -> InputConstants.KEY_PAGEDOWN;
            case KEY_PAGEUP -> InputConstants.KEY_PAGEUP;
            case KEY_CAPSLOCK -> InputConstants.KEY_CAPSLOCK;
            case KEY_PAUSE -> InputConstants.KEY_PAUSE;
            case KEY_SCROLLLOCK -> InputConstants.KEY_SCROLLLOCK;
            case KEY_PRINTSCREEN -> InputConstants.KEY_PRINTSCREEN;
        };
    }

    public static KeyBinding adapt(KeyMapping keyMapping) {
        if (keyMapping == null) {
            return null;
        }
        return new MinecraftKeyBinding(keyMapping);
    }

}
