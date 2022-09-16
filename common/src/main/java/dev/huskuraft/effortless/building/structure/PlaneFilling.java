package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PlaneFilling implements SingleSelectFeature {

    PLANE_FULL("plane_full"),
    PLANE_HOLLOW("plane_hollow");

    private final String name;

    PlaneFilling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.PLANE_FILLING.getName();
    }
}
