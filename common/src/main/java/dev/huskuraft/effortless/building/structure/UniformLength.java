package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum UniformLength implements SingleSelectFeature {
    DISABLE("disable"),
    LIMIT_2_TO_MIN("limit_2_to_min"),
    LIMIT_2_TO_MAX("limit_2_to_max"),
    LIMIT_3_TO_MIN("limit_3_to_min"),
    LIMIT_3_TO_MAX("limit_3_to_max"),
    ;

    private final String name;

    UniformLength(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.CIRCLE_START.getName();
    }
}
