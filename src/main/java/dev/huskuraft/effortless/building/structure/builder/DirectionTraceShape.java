package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.Direction;

public enum DirectionTraceShape {
    SINGLE,
    LINE_DOWN(Direction.DOWN),
    LINE_UP(Direction.UP),
    LINE_NORTH(Direction.NORTH),
    LINE_SOUTH(Direction.SOUTH),
    LINE_WEST(Direction.WEST),
    LINE_EAST(Direction.EAST),
    PLANE_X(Axis.X),
    PLANE_Y(Axis.Y),
    PLANE_Z(Axis.Z),
    CUBE;

    private final Axis axis;
    private final Direction direction;

    DirectionTraceShape() {
        this.axis = null;
        this.direction = null;
    }

    DirectionTraceShape(Axis axis) {
        this.axis = axis;
        this.direction = null;
    }

    DirectionTraceShape(Direction direction) {
        this.axis = direction.getAxis();
        this.direction = direction;
    }
}
