package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PlaneFacing implements SingleSelectFeature {
    BOTH("face_both"),
    VERTICAL("face_vertical"),
    HORIZONTAL("face_horizontal");

    private final String name;

    PlaneFacing(String name) {
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
            case BOTH -> true;
            case VERTICAL -> axis == Axis.X || axis == Axis.Z;
            case HORIZONTAL -> axis == Axis.Y;
        };
    }
}
