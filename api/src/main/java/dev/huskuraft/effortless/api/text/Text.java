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

    Text withStyle(TextStyle... styles);

    Text append(Text append);

    Text copy();

    String getString();

    default boolean isBlank() {
        return getString().isBlank();
    }

}
