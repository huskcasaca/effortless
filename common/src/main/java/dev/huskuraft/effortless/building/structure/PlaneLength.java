package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PlaneLength implements SingleSelectFeature {
    DISABLE("plane_length_disable"),
    LIMIT_TO_MAX("plane_length_limit_to_max"),
    ;

    private final String name;

    PlaneLength(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.PLANE_LENGTH.getName();
    }
}
