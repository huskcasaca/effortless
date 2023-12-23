package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.client.gui.Font;

class MinecraftTypeface extends Typeface {

    private final Font font;

    MinecraftTypeface(Font font) {
        this.font = font;
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
        return getRef().width(MinecraftClientAdapter.adapt(text));
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
