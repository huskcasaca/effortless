package dev.huskuraft.effortless.api.text;

import dev.huskuraft.effortless.api.platform.Entrance;

public interface Text {

    static Text empty() {
        return Entrance.getInstance().getPlatform().newText();
    }

    static Text text(String text) {
        return Entrance.getInstance().getPlatform().newText(text);
    }

    static Text text(String text, Text... args) {
        return Entrance.getInstance().getPlatform().newText(text, args);
    }

    static Text translate(String text) {
        return Entrance.getInstance().getPlatform().newTranslatableText(text);
    }

    static Text translate(String text, Text... args) {
        return Entrance.getInstance().getPlatform().newTranslatableText(text, args);
    }

    Text withStyle(TextStyle... styles);

    Text append(Text append);

    Text copy();

    String getString();

    default boolean isBlank() {
        return getString().isBlank();
    }

}
