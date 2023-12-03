package dev.huskuraft.effortless.core;

public enum Position {

    DISABLED("disabled"),
    LEFT("left"),
    RIGHT("right");

    private final String name;

    Position(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return "effortless.position.%s".formatted(name);
    }

    public AxisDirection getAxis() {
        return switch (this) {
            case LEFT -> AxisDirection.NEGATIVE;
            case RIGHT -> AxisDirection.POSITIVE;
            default -> null;
        };
    }
}
