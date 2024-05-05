package dev.huskuraft.effortless.api.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public enum ChatFormatting {
    BLACK("BLACK", '0', 0, 0),
    DARK_BLUE("DARK_BLUE", '1', 1, 170),
    DARK_GREEN("DARK_GREEN", '2', 2, 43520),
    DARK_AQUA("DARK_AQUA", '3', 3, 43690),
    DARK_RED("DARK_RED", '4', 4, 11141120),
    DARK_PURPLE("DARK_PURPLE", '5', 5, 11141290),
    GOLD("GOLD", '6', 6, 16755200),
    GRAY("GRAY", '7', 7, 11184810),
    DARK_GRAY("DARK_GRAY", '8', 8, 5592405),
    BLUE("BLUE", '9', 9, 5592575),
    GREEN("GREEN", 'a', 10, 5635925),
    AQUA("AQUA", 'b', 11, 5636095),
    RED("RED", 'c', 12, 16733525),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695),
    YELLOW("YELLOW", 'e', 14, 16777045),
    WHITE("WHITE", 'f', 15, 16777215),

    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),

    RESET("RESET", 'r', -1, null);

    public static final char PREFIX_CODE = '§';
    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Map<String, ChatFormatting> FORMATTING_BY_NAME = Arrays.stream(values()).collect(Collectors.toMap((textStyle) -> cleanName(textStyle.name), (textStyle) -> textStyle));
    private final String name;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private final int id;
    @Nullable
    private final Integer color;

    ChatFormatting(String name, char code, int id, @Nullable Integer color) {
        this(name, code, false, id, color);
    }

    ChatFormatting(String name, char code, boolean isFormat) {
        this(name, code, isFormat, -1, null);
    }

    ChatFormatting(String name, char code, boolean format, int id, @Nullable Integer color) {
        this.name = name;
        this.code = code;
        this.isFormat = format;
        this.id = id;
        this.color = color;
        this.toString = "§" + code;
    }

    private static String cleanName(String string) {
        return string.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    @Nullable
    public static String stripFormatting(@Nullable String text) {
        return text == null ? null : STRIP_FORMATTING_PATTERN.matcher(text).replaceAll("");
    }

    @Nullable
    public static ChatFormatting getByName(@Nullable String friendlyName) {
        return friendlyName == null ? null : FORMATTING_BY_NAME.get(cleanName(friendlyName));
    }

    @Nullable
    public static ChatFormatting getById(int index) {
        if (index < 0) {
            return RESET;
        } else {
            for (var textStyle : values()) {
                if (textStyle.getId() == index) {
                    return textStyle;
                }
            }

            return null;
        }
    }

    @Nullable
    public static ChatFormatting getByCode(char code) {
        var c0 = Character.toLowerCase(code);

        for (var textStyle : values()) {
            if (textStyle.code == c0) {
                return textStyle;
            }
        }

        return null;
    }

    public static Collection<String> getNames(boolean getColor, boolean getFancyStyling) {
        var list = new ArrayList<String>();

        for (var textStyle : values()) {
            if ((!textStyle.isColor() || getColor) && (!textStyle.isFormat() || getFancyStyling)) {
                list.add(textStyle.getName());
            }
        }

        return list;
    }

    public char getChar() {
        return this.code;
    }

    public int getId() {
        return this.id;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }

    @Nullable
    public Integer getColor() {
        return this.color;
    }

    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.toString;
    }
}
