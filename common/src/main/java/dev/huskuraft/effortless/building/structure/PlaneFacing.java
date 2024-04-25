package dev.huskuraft.effortless.building.structure;

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
}
