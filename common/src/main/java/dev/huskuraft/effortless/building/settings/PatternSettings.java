package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.pattern.Pattern;

import java.util.Collections;
import java.util.List;

public record PatternSettings(
        List<Pattern> patterns
) {

    public PatternSettings() {
        this(Collections.emptyList());
    }

}
