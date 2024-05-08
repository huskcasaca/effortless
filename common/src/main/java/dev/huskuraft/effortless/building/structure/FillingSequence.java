package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum FillingSequence implements SingleSelectFeature {
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
    public String getCategory() {
        return null;
    }
}
