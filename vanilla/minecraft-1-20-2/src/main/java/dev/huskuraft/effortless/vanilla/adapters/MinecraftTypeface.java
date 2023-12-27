package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.gui.Font;

public class MinecraftTypeface extends Typeface {

    private final Font font;

    MinecraftTypeface(Font font) {
        this.font = font;
    }

    public static Typeface fromMinecraftTypeface(Font font) {
        if (font == null) {
            return null;
        }
        return new MinecraftTypeface(font);
    }

    public static Font toMinecraftTypeface(Typeface typeface) {
        if (typeface == null) {
            return null;
        }
        return ((MinecraftTypeface) typeface).getRef();
    }

    public Font getRef() {
        return font;
    }

    @Override
    public int measureHeight(Text text) {
        return getRef().lineHeight;
    }

    @Override
    public int measureWidth(Text text) {
        return getRef().width(MinecraftText.toMinecraftText(text));
    }

    @Override
    public int measureHeight(String text) {
        return getRef().lineHeight;
    }

    @Override
    public int measureWidth(String text) {
        return getRef().width(text);
    }

    @Override
    public int getLineHeight() {
        return getRef().lineHeight;
    }

    @Override
    public String subtractByWidth(String text, int width, boolean tail) {
        return getRef().plainSubstrByWidth(text, width, tail);
    }

}
