package dev.huskuraft.effortless.building.structure;

public enum PlaneFilling implements BuildFeature {
    FILLED("plane_filled"),
    HOLLOW("plane_hollow");

    private final String name;

    PlaneFilling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BuildFeatures getType() {
        return BuildFeatures.PLANE_FILLING;
    }
}
