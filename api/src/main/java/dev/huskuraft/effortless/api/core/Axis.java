package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.text.Text;

public enum Axis {
    X("x") {
        public int choose(int x, int y, int z) {
            return x;
        }

        public double choose(double x, double y, double z) {
            return x;
        }
    },
    Y("y") {
        public int choose(int x, int y, int z) {
            return y;
        }

        public double choose(double x, double y, double z) {
            return y;
        }
    },
    Z("z") {
        public int choose(int x, int y, int z) {
            return z;
        }

        public double choose(double x, double y, double z) {
            return z;
        }
    };

    private final String name;

    Axis(String name) {
        this.name = name;
    }

    public Text getDisplayName() {
        return Text.translate("effortless.axis.%s".formatted(name));
    }

    public String getName() {
        return name;
    }

    public boolean isVertical() {
        return this == Y;
    }

    public boolean isHorizontal() {
        return this == X || this == Z;
    }

    public String toString() {
        return this.name;
    }

    public PlaneDirection getPlane() {
        return switch (this) {
            case X, Z -> PlaneDirection.HORIZONTAL;
            case Y -> PlaneDirection.VERTICAL;
        };
    }

    @Deprecated
    public abstract int choose(int x, int y, int z);

    public abstract double choose(double x, double y, double z);
}
