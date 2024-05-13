package dev.huskuraft.effortless.api.text;

import java.util.Collection;
import java.util.stream.Stream;

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

    Style getStyle();

    Text withStyle(Style style);

    default Text withStyle(ChatFormatting... styles) {
        return withStyle(getStyle().applyFormat(styles));
    }

    default Text withColor(Integer color) {
        return withStyle(getStyle().withColor(color));
    }

    String getString();

    Collection<Text> getSiblings();

    Text withSiblings(Collection<Text> siblings);

    default Text append(Text text) {
        return withSiblings(Stream.concat(getSiblings().stream(), Stream.of(text)).toList());
    }

    Text copy();

    void decompose(Sink sink);

    interface Sink {
        boolean accept(int index, String text, Style style);
    }

}
