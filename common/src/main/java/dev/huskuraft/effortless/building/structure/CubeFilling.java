package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum CubeFilling implements SingleSelectFeature {
    CUBE_FULL("cube_full"),
    CUBE_HOLLOW("cube_hollow"),
    CUBE_SKELETON("cube_skeleton");

    private final String name;

    CubeFilling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.CUBE_FILLING.getName();
    }
}
