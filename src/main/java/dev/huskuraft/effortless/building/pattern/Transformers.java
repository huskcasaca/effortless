package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.universal.api.text.Text;

public enum Transformers {
    ARRAY("array"),
    MIRROR("mirror"),
    RADIAL("radial"),
    RANDOMIZER("randomizer");

    private final String name;

    Transformers(String name) {
        this.name = name;
    }

    public Text getDisplayName() {
        return Text.translate("effortless.transformer.%s".formatted(name));
    }

    public Text getTitleText() {
        return Text.translate("effortless.transformer.%s.title".formatted(name));
    }

}
