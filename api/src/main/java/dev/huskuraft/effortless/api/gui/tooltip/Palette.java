package dev.huskuraft.effortless.api.gui.tooltip;

import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Style;

public record Palette(Style primary, Style highlight) {

    public static Style styleFromColor(ChatFormatting color) {
        return Style.EMPTY.applyFormat(color);
    }

    public static Style styleFromColor(int hex) {
        return Style.EMPTY.withColor(hex);
    }

    public static final Palette BLUE = ofColors(ChatFormatting.BLUE, ChatFormatting.AQUA);
    public static final Palette GREEN = ofColors(ChatFormatting.DARK_GREEN, ChatFormatting.GREEN);
    public static final Palette YELLOW = ofColors(ChatFormatting.GOLD, ChatFormatting.YELLOW);
    public static final Palette RED = ofColors(ChatFormatting.DARK_RED, ChatFormatting.RED);
    public static final Palette PURPLE = ofColors(ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE);
    public static final Palette GRAY = ofColors(ChatFormatting.DARK_GRAY, ChatFormatting.GRAY);

    public static final Palette ALL_GRAY = ofColors(ChatFormatting.GRAY, ChatFormatting.GRAY);
    public static final Palette GRAY_AND_BLUE = ofColors(ChatFormatting.GRAY, ChatFormatting.BLUE);
    public static final Palette GRAY_AND_WHITE = ofColors(ChatFormatting.GRAY, ChatFormatting.WHITE);
    public static final Palette GRAY_AND_GOLD = ofColors(ChatFormatting.GRAY, ChatFormatting.GOLD);
    public static final Palette GRAY_AND_RED = ofColors(ChatFormatting.GRAY, ChatFormatting.RED);

    public static Palette ofColors(ChatFormatting primary, ChatFormatting highlight) {
        return new Palette(styleFromColor(primary), styleFromColor(highlight));
    }
}
