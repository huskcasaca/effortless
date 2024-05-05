package dev.huskuraft.effortless.api.gui.tooltip;

import dev.huskuraft.effortless.api.text.Style;
import dev.huskuraft.effortless.api.text.TextStyle;

public record Palette(Style primary, Style highlight) {

    public static Style styleFromColor(TextStyle color) {
        return Style.EMPTY.applyFormat(color);
    }

    public static Style styleFromColor(int hex) {
        return Style.EMPTY.withColor(hex);
    }

    public static final Palette STANDARD_ES = new Palette(styleFromColor(0xC9974C), styleFromColor(0xF1DD79));

    public static final Palette BLUE = ofColors(TextStyle.BLUE, TextStyle.AQUA);
    public static final Palette GREEN = ofColors(TextStyle.DARK_GREEN, TextStyle.GREEN);
    public static final Palette YELLOW = ofColors(TextStyle.GOLD, TextStyle.YELLOW);
    public static final Palette RED = ofColors(TextStyle.DARK_RED, TextStyle.RED);
    public static final Palette PURPLE = ofColors(TextStyle.DARK_PURPLE, TextStyle.LIGHT_PURPLE);
    public static final Palette GRAY = ofColors(TextStyle.DARK_GRAY, TextStyle.GRAY);

    public static final Palette ALL_GRAY = ofColors(TextStyle.GRAY, TextStyle.GRAY);
    public static final Palette GRAY_AND_BLUE = ofColors(TextStyle.GRAY, TextStyle.BLUE);
    public static final Palette GRAY_AND_WHITE = ofColors(TextStyle.GRAY, TextStyle.WHITE);
    public static final Palette GRAY_AND_GOLD = ofColors(TextStyle.GRAY, TextStyle.GOLD);
    public static final Palette GRAY_AND_RED = ofColors(TextStyle.GRAY, TextStyle.RED);

    public static Palette ofColors(TextStyle primary, TextStyle highlight) {
        return new Palette(styleFromColor(primary), styleFromColor(highlight));
    }
}
