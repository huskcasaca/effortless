package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.text.Text;

public enum Position {

    DISABLED("disabled"),
    LEFT("left"),
    RIGHT("right");

    private final String name;

    Position(String name) {
        this.name = name;
    }

    public String getNameKey() {
        // TODO: 15/9/22 use ResourceLocation
        return Text.asKey("position", name);
    }

    public AxisDirection getAxis() {
        return switch (this) {
            case LEFT -> AxisDirection.NEGATIVE;
            case RIGHT -> AxisDirection.POSITIVE;
            default -> null;
        };
    }
}
