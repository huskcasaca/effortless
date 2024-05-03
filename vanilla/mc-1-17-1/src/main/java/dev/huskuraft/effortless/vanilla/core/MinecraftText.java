package dev.huskuraft.effortless.vanilla.core;

import java.util.Arrays;

import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public record MinecraftText(Component referenceValue) implements Text {

    public static Text ofNullable(Component reference) {
        return reference == null ? null : new MinecraftText(reference);
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


    @Override
    public Text withStyle(TextStyle... styles) {
        return new MinecraftText(referenceValue().copy().withStyle(Arrays.stream(styles).map(MinecraftText::adapt).toArray(ChatFormatting[]::new)));
    }

    @Override
    public Text append(Text append) {
        return new MinecraftText(referenceValue().copy().append(append.<Component>reference()));
    }

    @Override
    public Text copy() {
        return new MinecraftText(referenceValue().copy());
    }

    @Override
    public String getString() {
        return referenceValue().getString();
    }

}
