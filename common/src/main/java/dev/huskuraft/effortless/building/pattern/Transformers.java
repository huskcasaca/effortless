package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.text.Text;

public enum Transformers {
    ARRAY("array"),
    MIRROR("mirror"),
    RADIAL("radial"),
    ITEM_RAND("item_rand");

    private final String name;

    Transformers(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return "effortless.transformer.%s".formatted(getName());
    }

    public String getName() {
        return name;
    }

}
