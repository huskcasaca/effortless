package dev.huskuraft.effortless.gui;

import dev.huskuraft.effortless.text.Text;

public abstract class Typeface {

    public abstract int measureHeight(Text text);

    public abstract int measureWidth(Text text);

    public abstract int measureHeight(String text);

    public abstract int measureWidth(String text);

    public abstract int getLineHeight();

    public abstract String subtractByWidth(String text, int width, boolean tail);

}
