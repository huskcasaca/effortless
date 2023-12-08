package dev.huskuraft.effortless.text;

import dev.huskuraft.effortless.Effortless;

public abstract class Text {

    public static Text empty() {
        return Effortless.getInstance().getContentCreator().text();
    }

    public static Text text(String text) {
        return Effortless.getInstance().getContentCreator().text(text);
    }

    public static Text text(String text, Text... args) {
        return Effortless.getInstance().getContentCreator().text(text, args);
    }

    public static Text translate(String text) {
        return Effortless.getInstance().getContentCreator().translate(text);
    }

    public static Text translate(String text, Text... args) {
        return Effortless.getInstance().getContentCreator().translate(text, args);
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
