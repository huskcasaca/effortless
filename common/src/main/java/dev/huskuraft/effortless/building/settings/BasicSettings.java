package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.structure.BuildMode;

import java.util.Collections;
import java.util.List;

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
