package dev.huskuraft.effortless.building.config;

import dev.huskuraft.universal.api.math.Range1i;

public record BuilderConfig(
        int reservedToolDurability,
        boolean passiveMode
) {

    public static final Range1i RESERVED_TOOL_DURABILITY_RANGE = new Range1i(0, 32);

    public static BuilderConfig DEFAULT = new BuilderConfig(
            1,
            Boolean.FALSE
    );

    public BuilderConfig withReservedToolDurability(int reservedToolDurability) {
        return new BuilderConfig(reservedToolDurability, passiveMode);
    }

    public BuilderConfig withPassiveMode(boolean passiveMode) {
        return new BuilderConfig(reservedToolDurability, passiveMode);
    }


}
