package dev.huskuraft.effortless.text;

import dev.huskuraft.effortless.Effortless;

public abstract class Text {

    public static Text empty() {
        return Effortless.getInstance().getPlatform().newText();
    }

    public static Text text(String text) {
        return Effortless.getInstance().getPlatform().newText(text);
    }

    public static Text text(String text, Text... args) {
        return Effortless.getInstance().getPlatform().newText(text, args);
    }

    public static Text translate(String text) {
        return Effortless.getInstance().getPlatform().newTranslatableText(text);
    }

    public static Text translate(String text, Text... args) {
        return Effortless.getInstance().getPlatform().newTranslatableText(text, args);
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
