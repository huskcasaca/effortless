package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.universal.api.core.Axis;

public enum TraceShape {
    SINGLE,
    LINE_X(Axis.X),
    LINE_Y(Axis.Y),
    LINE_Z(Axis.Z),
    PLANE_X(Axis.X),
    PLANE_Y(Axis.Y),
    PLANE_Z(Axis.Z),
    CUBE;

    private final Axis axis;

    TraceShape() {
        this.axis = null;
    }

    TraceShape(Axis axis) {
        this.axis = axis;
    }

    public static TraceShape fromPosition(int x1, int y1, int z1, int x2, int y2, int z2) {
        if (x1 == x2 && y1 == y2 && z1 == z2) {
            return TraceShape.SINGLE;
        }
        if (x1 == x2 && z1 == z2) {
            return TraceShape.LINE_Y;
        }
        if (y1 == y2 && z1 == z2) {
            return TraceShape.LINE_X;
        }
        if (x1 == x2 && y1 == y2) {
            return TraceShape.LINE_Z;
        }
        if (x1 == x2) {
            return PLANE_X;
        }
        if (y1 == y2) {
            return PLANE_Y;
        }
        if (z1 == z2) {
            return PLANE_Z;
        }
        return TraceShape.CUBE;
    }
}
