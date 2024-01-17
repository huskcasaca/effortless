package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class MinecraftText implements Text {

    private final Component reference;

    MinecraftText(Component reference) {
        this.reference = reference;
    }

    public static Text fromMinecraftText(Component value) {
        if (value == null) {
            return null;
        }
        return new MinecraftText(value);
    }

    public static Component toMinecraftText(Text value) {
        if (value == null) {
            return null;
        }
        return ((MinecraftText) value).reference;
    }

    @Override
    public Component referenceValue() {
        return reference;
    }

    @Override
    public Text withStyle(TextStyle... styles) {
        return fromMinecraftText(reference.copy().withStyle(Arrays.stream(styles).map(MinecraftText::adapt).toArray(ChatFormatting[]::new)));
    }

    @Override
    public Text append(Text append) {
        return fromMinecraftText(reference.copy().append(toMinecraftText(append)));
    }

    @Override
    public Text copy() {
        return fromMinecraftText(reference.copy());
    }

    @Override
    public String getString() {
        return reference.getString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftText text && reference.equals(text.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public String toString() {
        return getString();
    }

    private static ChatFormatting adapt(TextStyle textStyle) {
        return switch (textStyle) {
            case BLACK -> ChatFormatting.BLACK;
            case DARK_BLUE -> ChatFormatting.DARK_BLUE;
            case DARK_GREEN -> ChatFormatting.DARK_GREEN;
            case DARK_AQUA -> ChatFormatting.DARK_AQUA;
            case DARK_RED -> ChatFormatting.DARK_RED;
            case DARK_PURPLE -> ChatFormatting.DARK_PURPLE;
            case GOLD -> ChatFormatting.GOLD;
            case GRAY -> ChatFormatting.GRAY;
            case DARK_GRAY -> ChatFormatting.DARK_GRAY;
            case BLUE -> ChatFormatting.BLUE;
            case GREEN -> ChatFormatting.GREEN;
            case AQUA -> ChatFormatting.AQUA;
            case RED -> ChatFormatting.RED;
            case LIGHT_PURPLE -> ChatFormatting.LIGHT_PURPLE;
            case YELLOW -> ChatFormatting.YELLOW;
            case WHITE -> ChatFormatting.WHITE;
            case OBFUSCATED -> ChatFormatting.OBFUSCATED;
            case BOLD -> ChatFormatting.BOLD;
            case STRIKETHROUGH -> ChatFormatting.STRIKETHROUGH;
            case UNDERLINE -> ChatFormatting.UNDERLINE;
            case ITALIC -> ChatFormatting.ITALIC;
            case RESET -> ChatFormatting.RESET;
        };
    }
}
