package dev.huskuraft.effortless.building.structure;

public enum PlaneLength implements BuildFeature {
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
    public BuildFeatures getType() {
        return BuildFeatures.PLANE_LENGTH;
    }
}
