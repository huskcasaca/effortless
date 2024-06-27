package dev.huskuraft.effortless.vanilla.gui;

import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public record MinecraftTypeface(
        Font refs
) implements Typeface {

    @Override
    public int measureHeight(Text text) {
        return refs.lineHeight;
    }

    @Override
    public int measureWidth(Text text) {
        return refs.width((Component) text.reference());
    }

    @Override
    public int measureHeight(String text) {
        return refs.lineHeight;
    }

    @Override
    public int measureWidth(String text) {
        return refs.width(text);
    }

    @Override
    public int getLineHeight() {
        return refs.lineHeight;
    }

    @Override
    public String subtractByWidth(String text, int width, boolean tail) {
        return refs.plainSubstrByWidth(text, width, tail);
    }

}
