package dev.huskuraft.effortless.api.math;

public record Range1i(int min, int max) {

    public static final Range1i UNBOUNDED = new Range1i(Integer.MIN_VALUE, Integer.MAX_VALUE);

    public boolean contains(int value) {
        return (value >= min && value <= max);
    }

    public boolean isBelow(int value) {
        return (value < min);
    }

    public boolean isAbove(int value) {
        return (value > max);
    }
}
