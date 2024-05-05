package dev.huskuraft.effortless.api.text;

import javax.annotation.Nullable;

public record Style(
        @Nullable Integer color,
        @Nullable Boolean bold,
        @Nullable Boolean italic,
        @Nullable Boolean underlined,
        @Nullable Boolean strikethrough,
        @Nullable Boolean obfuscated
/*        @Nullable ClickEvent clickEvent,
        @Nullable HoverEvent hoverEvent,
        @Nullable String insertion,
        @Nullable ResourceLocation font*/
) {

    public static Style EMPTY = new Style(null, null, null, null, null, null/*, null, null, null, null*/);

    public Style applyFormat(TextStyle textStyle) {
        var color = this.color;
        var bold = this.bold;
        var italic = this.italic;
        var strikethrough = this.strikethrough;
        var underlined = this.underlined;
        var obfuscated = this.obfuscated;
        switch (textStyle) {
            case OBFUSCATED -> obfuscated = true;
            case BOLD -> bold = true;
            case STRIKETHROUGH -> strikethrough = true;
            case UNDERLINE -> underlined = true;
            case ITALIC -> italic = true;
            case RESET -> {
                return EMPTY;
            }
            default -> {
                color = textStyle.getColor();
            }
        }

        return new Style(color, bold, italic, underlined, strikethrough, obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style applyFormat(TextStyle... textStyle) {
        var style = this;
        for (TextStyle style1 : textStyle) {
            style = style.applyFormat(style1);
        }
        return style;
    }

    public Style withColor(@Nullable Integer color) {
        return new Style(color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withColor(TextStyle color) {
        return new Style(color.getColor(), this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withBold(@Nullable Boolean bold) {
        return new Style(this.color, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withItalic(@Nullable Boolean italic) {
        return new Style(this.color, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withUnderlined(@Nullable Boolean underlined) {
        return new Style(this.color, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withStrikethrough(@Nullable Boolean strikethrough) {
        return new Style(this.color, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }

    public Style withObfuscated(@Nullable Boolean obfuscated) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated/*, this.clickEvent, this.hoverEvent, this.insertion, this.font*/);
    }


}
