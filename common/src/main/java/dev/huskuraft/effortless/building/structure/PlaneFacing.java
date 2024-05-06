package dev.huskuraft.effortless.building.structure;

import java.util.Set;

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

    public Set<Axis> getAxes() {
        return switch (this) {
            case BOTH -> Set.of(Axis.X, Axis.Y, Axis.Z);
            case VERTICAL -> Set.of(Axis.X, Axis.Z);
            case HORIZONTAL -> Set.of(Axis.Y);
        };
    }
}
