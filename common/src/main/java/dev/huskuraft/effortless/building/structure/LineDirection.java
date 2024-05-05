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
        return switch (axis) {
            case X -> this == ALL || this == FLOOR || this == AXIS_X;
            case Y -> this == ALL || this == AXIS_Y;
            case Z -> this == ALL || this == FLOOR || this == AXIS_Z;
        };
    }
}
