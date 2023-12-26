package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.input.KeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;

public class MinecraftClientAdapter extends MinecraftAdapter {

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

    public static KeyBinding adapt(KeyMapping keyMapping) {
        if (keyMapping == null) {
            return null;
        }
        return new MinecraftKeyBinding(keyMapping);
    }

    public static KeyMapping adapt(KeyBinding keyBinding) {
        if (keyBinding == null) {
            return null;
        }
        return ((MinecraftKeyBinding) keyBinding).getRef();
    }


}
