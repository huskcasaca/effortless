package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum CircleStart implements SingleSelectFeature {
    CIRCLE_START_CORNER("circle_start_corner"),
    CIRCLE_START_CENTER("circle_start_center"),
    ;

    private final String name;

    CircleStart(String name) {
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
