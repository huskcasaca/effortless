package dev.huskuraft.effortless.building.structure;

public enum CubeFilling implements BuildFeature {
    FILLED("cube_filled"),
    HOLLOW("cube_hollow"),
    SKELETON("cube_skeleton");

    private final String name;

    CubeFilling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BuildFeatures getType() {
        return BuildFeatures.CUBE_FILLING;
    }
}
