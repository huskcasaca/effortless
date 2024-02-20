package dev.huskuraft.effortless.vanilla.gui;

import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class MinecraftTypeface implements Typeface {

    private final Font reference;

    public MinecraftTypeface(Font reference) {
        this.reference = reference;
    }

    @Override
    public Font referenceValue() {
        return reference;
    }

    @Override
    public int measureHeight(Text text) {
        return reference.lineHeight;
    }

    @Override
    public int measureWidth(Text text) {
        return reference.width((Component) text.reference());
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftTypeface typeface && reference.equals(typeface.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
