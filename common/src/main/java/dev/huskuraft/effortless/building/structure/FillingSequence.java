package dev.huskuraft.effortless.building.structure;

public enum FillingSequence implements BuildFeature {
    X_Y_Z,
    X_Z_Y,
    Y_X_Z,
    Y_Z_X,
    Z_X_Y,
    Z_Y_X;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public BuildFeatures getType() {
        return null;
    }
}
