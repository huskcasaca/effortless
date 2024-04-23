package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum UniformLength implements SingleSelectFeature {
    DISABLE("uniform_length_disable"),
    LIMIT_TO_MIN("uniform_length_limit_to_min"),
    LIMIT_TO_MAX("uniform_length_limit_to_max"),
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
        return BuildFeature.UNIFORM_LENGTH.getName();
    }
}
