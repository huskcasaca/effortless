package dev.huskuraft.effortless.api.text;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Text extends PlatformReference {

    static Text empty() {
        return Entrance.getInstance().getContentFactory().newText();
    }

    static Text text(String text) {
        return Entrance.getInstance().getContentFactory().newText(text);
    }

    static Text text(String text, Text... args) {
        return Entrance.getInstance().getContentFactory().newText(text, args);
    }

    static Text translate(String text) {
        return Entrance.getInstance().getContentFactory().newTranslatableText(text);
    }

    static Text translate(String text, Text... args) {
        return Entrance.getInstance().getContentFactory().newTranslatableText(text, args);
    }

    Text withStyle(TextStyle... styles);

    Text append(Text append);

    Text copy();

    String getString();

    default boolean isBlank() {
        return getString().isBlank();
    }

}
