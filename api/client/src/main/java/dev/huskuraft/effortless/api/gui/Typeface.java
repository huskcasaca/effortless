package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.text.Text;

public interface Typeface extends PlatformReference {

    int measureHeight(Text text);

    int measureWidth(Text text);

    int measureHeight(String text);

    int measureWidth(String text);

    int getLineHeight();

    String subtractByWidth(String text, int width, boolean tail);

}
