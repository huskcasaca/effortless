package dev.huskuraft.effortless.building.settings;

import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public record RandomizerSettings(
        List<ItemRandomizer> randomizers
) {

    public RandomizerSettings() {
        this(Collections.emptyList());
    }

}
