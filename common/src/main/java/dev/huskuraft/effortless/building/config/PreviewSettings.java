package dev.huskuraft.effortless.building.config;

import dev.huskuraft.effortless.api.core.Position;

public record PreviewSettings(
    int itemUsagePosition,
    int buildInfoPosition,
    boolean alwaysShowBlockPreview,
    int shaderDissolveTimeMultiplier
) {

    public static final int MIN_SHADER_DISSOLVE_TIME_MULTIPLIER = 1;
    public static final int MAX_SHADER_DISSOLVE_TIME_MULTIPLIER = 40;

    public PreviewSettings() {
        this(
                Position.RIGHT.ordinal(),
                Position.RIGHT.ordinal(),
                false,
                10
        );
    }

}
