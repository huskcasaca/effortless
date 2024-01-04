package dev.huskuraft.effortless.api.core;

public enum PlaneDirection {
    HORIZONTAL(new Orientation[]{Orientation.NORTH, Orientation.EAST, Orientation.SOUTH, Orientation.WEST}, new Axis[]{Axis.X, Axis.Z}),
    VERTICAL(new Orientation[]{Orientation.UP, Orientation.DOWN}, new Axis[]{Axis.Y});

    private final Orientation[] faces;
    private final Axis[] axis;

    PlaneDirection(Orientation[] orientations, Axis[] axiss) {
        this.faces = orientations;
        this.axis = axiss;
    }

}
