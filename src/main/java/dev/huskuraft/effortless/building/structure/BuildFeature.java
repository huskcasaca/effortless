package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public interface BuildFeature extends SingleSelectFeature {

    @Override
    default String getCategory() {
        return getType().getName();
    }

    BuildFeatures getType();

}
