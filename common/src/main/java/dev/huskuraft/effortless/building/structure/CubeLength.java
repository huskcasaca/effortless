package dev.huskuraft.effortless.building.structure;

public enum CubeLength implements BuildFeature {
    VARIABLE("cube_length_variable"),
    EQUAL("cube_length_equal"),
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
        return BuildFeatures.CUBE_LENGTH;
    }
}
