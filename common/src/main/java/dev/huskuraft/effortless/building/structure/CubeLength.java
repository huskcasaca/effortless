package dev.huskuraft.effortless.building.structure;

public enum CubeLength implements BuildFeature {
    DISABLE("cube_length_disable"),
    LIMIT_TO_MAX("cube_length_limit_to_max"),
    ;

    private final String name;

    CubeLength(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BuildFeatures getType() {
        return BuildFeatures.PLANE_LENGTH;
    }
}
