package dev.huskuraft.effortless.api.text;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Text extends PlatformReference {

    static Text empty() {
        return ContentFactory.getInstance().newText();
    }

    static Text text(String text) {
        return ContentFactory.getInstance().newText(text);
    }

    static Text translate(String text) {
        return ContentFactory.getInstance().newTranslatableText(text);
    }

    static Text translate(String text, Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Text text1)
                args[i] = (text1).reference();
        }
        return ContentFactory.getInstance().newTranslatableText(text, args);
    }

    default Style getStyle() {
        return new Style(getColor(), isBold(), isItalic(), isUnderlined(), isStrikethrough(), isObfuscated());
    }

    default Text withStyle(Style style) {
        return withBold(style.bold()).withItalic(style.italic()).withUnderlined(style.underlined()).withStrikethrough(style.strikethrough()).withObfuscated(style.obfuscated()).withColor(style.color());
    }

    default Text withStyle(TextStyle... styles) {
        return withStyle(getStyle().applyFormat(styles));
    }

    Text withBold(Boolean bold);
    Text withItalic(Boolean italic);
    Text withUnderlined(Boolean underlined);
    Text withStrikethrough(Boolean strikethrough);
    Text withObfuscated(Boolean obfuscated);
    Text withColor(Integer color);

    Boolean isBold();

    Boolean isItalic();

    Boolean isUnderlined();

    Boolean isStrikethrough();

    Boolean isObfuscated();

    Integer getColor();

    String getString();

}
