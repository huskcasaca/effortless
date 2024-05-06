package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum LineDirection implements SingleSelectFeature {
    ALL("line_direction_all"),
    FLOOR("line_direction_floor"),
    AXIS_Y("line_direction_axis_y"),
    AXIS_Z("line_direction_axis_z"),
    AXIS_X("line_direction_axis_x");

    private final String name;

    LineDirection(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.PLANE_FACING.getName();
    }

    public boolean isInRange(Axis axis) {
        return switch (this) {
            case ALL -> true;
            case FLOOR -> axis == Axis.X || axis == Axis.Z;
            case AXIS_Y -> axis == Axis.Y;
            case AXIS_Z -> axis == Axis.Z;
            case AXIS_X -> axis == Axis.X;
        };
    }
}
