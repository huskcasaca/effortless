package dev.huskuraft.effortless.api.text;

import dev.huskuraft.effortless.api.platform.Entrance;

public abstract class Text {

    public static Text empty() {
        return Entrance.getInstance().getPlatform().newText();
    }

    public static Text text(String text) {
        return Entrance.getInstance().getPlatform().newText(text);
    }

    public static Text text(String text, Text... args) {
        return Entrance.getInstance().getPlatform().newText(text, args);
    }

    public static Text translate(String text) {
        return Entrance.getInstance().getPlatform().newTranslatableText(text);
    }

    public static Text translate(String text, Text... args) {
        return Entrance.getInstance().getPlatform().newTranslatableText(text, args);
    }

    public abstract Text withStyle(TextStyle... styles);

    public abstract Text append(Text append);

    public abstract Text copy();

    public abstract String getString();

    public boolean isBlank() {
        return getString().isBlank();
    }

    @Override
    public String toString() {
        return getString();
    }
}
