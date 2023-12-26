package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class MinecraftText extends Text {

    private final Component component;

    MinecraftText(Component component) {
        this.component = component;
    }

    public Component getRef() {
        return component;
    }

    @Override
    public Text withStyle(TextStyle... styles) {
        return MinecraftAdapter.adapt(component.copy().withStyle(Arrays.stream(styles).map(MinecraftText::adapt).toArray(ChatFormatting[]::new)));
    }

    @Override
    public Text append(Text append) {
        return MinecraftAdapter.adapt(component.copy().append(MinecraftAdapter.adapt(append)));
    }

    @Override
    public Text copy() {
        return MinecraftAdapter.adapt(component.copy());
    }

    @Override
    public String getString() {
        return getRef().getString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftText fabricText && getRef().equals(fabricText.getRef());
    }

    @Override
    public int hashCode() {
        return getRef().hashCode();
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
