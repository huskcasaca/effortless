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

    public TextStyle recoveryColor(int color) {
        for (var value : TextStyle.values()) {
            if (value.getColor() != null && value.getColor().equals(color)) {
                return value;
            }
        }
        return null;
    }

    public String getPrefixString() {
        var builder = new StringBuilder();
        if (Boolean.TRUE.equals(obfuscated)) {
            builder.append(TextStyle.OBFUSCATED);
        }
        if (Boolean.TRUE.equals(bold)) {
            builder.append(TextStyle.BOLD);
        }
        if (Boolean.TRUE.equals(italic)) {
            builder.append(TextStyle.ITALIC);
        }
        if (Boolean.TRUE.equals(underlined)) {
            builder.append(TextStyle.UNDERLINE);
        }
        if (Boolean.TRUE.equals(strikethrough)) {
            builder.append(TextStyle.STRIKETHROUGH);
        }
        if (color != null && recoveryColor(color) != null) {
            builder.append(recoveryColor(color));
        }
        return builder.toString();
    }

    public String getSuffixString() {
        if (getPrefixString().isEmpty()) {
            return "";
        }
        return TextStyle.RESET.toString();
    }

}
