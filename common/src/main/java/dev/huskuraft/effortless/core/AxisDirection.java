package dev.huskuraft.effortless.core;

public enum AxisDirection {
    POSITIVE("positive", 1),
    NEGATIVE("negative", -1);

    private final String name;
    private final int step;

    AxisDirection(String name, int step) {
        this.name = name;
        this.step = step;
    }

    public int getStep() {
        return this.step;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public AxisDirection opposite() {
        return this == POSITIVE ? NEGATIVE : POSITIVE;
    }
}
