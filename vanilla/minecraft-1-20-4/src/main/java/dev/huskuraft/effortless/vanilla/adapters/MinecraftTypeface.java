package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.gui.Font;

public class MinecraftTypeface extends Typeface {

    private final Font reference;

    MinecraftTypeface(Font reference) {
        this.reference = reference;
    }

    public static Typeface fromMinecraftTypeface(Font value) {
        if (value == null) {
            return null;
        }
        return new MinecraftTypeface(value);
    }

    public static Font toMinecraftTypeface(Typeface value) {
        if (value == null) {
            return null;
        }
        return ((MinecraftTypeface) value).reference;
    }

    @Override
    public int measureHeight(Text text) {
        return reference.lineHeight;
    }

    @Override
    public int measureWidth(Text text) {
        return reference.width(MinecraftText.toMinecraftText(text));
    }

    @Override
    public int measureHeight(String text) {
        return reference.lineHeight;
    }

    @Override
    public int measureWidth(String text) {
        return reference.width(text);
    }

    @Override
    public int getLineHeight() {
        return reference.lineHeight;
    }

    @Override
    public String subtractByWidth(String text, int width, boolean tail) {
        return reference.plainSubstrByWidth(text, width, tail);
    }

}
