package dev.huskuraft.effortless.building.settings;

import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.building.structure.BuildMode;

public record BasicSettings(
        List<BuildMode> modes
) {

    public BasicSettings() {
        this(Collections.emptyList());
    }

    public static BasicSettings getDefault() {
        return new BasicSettings(Collections.emptyList());
    }

}
