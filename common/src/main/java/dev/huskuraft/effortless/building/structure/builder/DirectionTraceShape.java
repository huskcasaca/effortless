package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Orientation;

public enum DirectionTraceShape {
    SINGLE,
    LINE_DOWN(Orientation.DOWN),
    LINE_UP(Orientation.UP),
    LINE_NORTH(Orientation.NORTH),
    LINE_SOUTH(Orientation.SOUTH),
    LINE_WEST(Orientation.WEST),
    LINE_EAST(Orientation.EAST),
    PLANE_X(Axis.X),
    PLANE_Y(Axis.Y),
    PLANE_Z(Axis.Z),
    CUBE;

    private final Axis axis;
    private final Orientation orientation;

    DirectionTraceShape() {
        this.axis = null;
        this.orientation = null;
    }

    DirectionTraceShape(Axis axis) {
        this.axis = axis;
        this.orientation = null;
    }

    DirectionTraceShape(Orientation orientation) {
        this.axis = orientation.getAxis();
        this.orientation = orientation;
    }
}
