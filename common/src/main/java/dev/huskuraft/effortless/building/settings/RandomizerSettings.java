package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

import java.util.Collections;
import java.util.List;

public record RandomizerSettings(
        List<ItemRandomizer> randomizers
) {

    public RandomizerSettings() {
        this(Collections.emptyList());
    }

}
