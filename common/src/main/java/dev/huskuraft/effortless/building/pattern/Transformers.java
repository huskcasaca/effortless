package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.text.Text;

public enum Transformers {
    ARRAY("array"),
    MIRROR("mirror"),
    RADIAL("radial"),
    RANDOMIZE("randomize");

    private final String name;

    Transformers(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return Text.asKey("transformer", getName());
    }

    public String getName() {
        return name;
    }

}
