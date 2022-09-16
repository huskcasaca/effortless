package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PlaneFacing implements SingleSelectFeature {
    HORIZONTAL("face_horizontal"),
    VERTICAL("face_vertical"),
    BOTH("face_both");

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
}
