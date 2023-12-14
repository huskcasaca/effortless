package dev.huskuraft.effortless.math;

public record Range1d(double low, double high) {

    public static final Range1d UNBOUNDED = new Range1d(Double.MIN_VALUE, Double.MAX_VALUE);

    public boolean contains(double value) {
        return (value >= low && value <= high);
    }

    public boolean isBelow(double value) {
        return (value < low);
    }

    public boolean isAbove(double value) {
        return (value > high);
    }
}
