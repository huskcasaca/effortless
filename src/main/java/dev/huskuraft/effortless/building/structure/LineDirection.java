package dev.huskuraft.effortless.building.structure;

import java.util.Set;

import dev.huskuraft.universal.api.core.Axis;

public enum LineDirection implements BuildFeature {
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
    public BuildFeatures getType() {
        return BuildFeatures.LINE_DIRECTION;
    }

    public Set<Axis> getAxes() {
        return switch (this) {
            case ALL -> Set.of(Axis.X, Axis.Y, Axis.Z);
            case FLOOR -> Set.of(Axis.X, Axis.Z);
            case AXIS_Y -> Set.of(Axis.Y);
            case AXIS_Z -> Set.of(Axis.Z);
            case AXIS_X -> Set.of(Axis.X);
        };
    }
}
