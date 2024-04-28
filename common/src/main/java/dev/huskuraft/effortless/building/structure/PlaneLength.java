package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PlaneLength implements SingleSelectFeature {
    VARIABLE("plane_length_variable"),
    EQUAL("plane_length_equal"),
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
