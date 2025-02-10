package dev.huskuraft.effortless.building;

import dev.huskuraft.universal.api.text.Text;

public enum PositionType {
    ABSOLUTE("absolute"),
    RELATIVE("relative");

    private final String name;

    PositionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Text getDisplayName() {
        return Text.translate("effortless.position.%s".formatted(name));
    }

}
