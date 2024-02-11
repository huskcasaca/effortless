package dev.huskuraft.effortless.building.settings;

import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.building.pattern.Pattern;

public record PatternSettings(
        List<Pattern> patterns
) {

    public PatternSettings() {
        this(Collections.emptyList());
    }

}
