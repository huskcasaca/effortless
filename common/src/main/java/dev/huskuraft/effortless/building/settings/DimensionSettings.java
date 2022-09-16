package dev.huskuraft.effortless.building.settings;

public record DimensionSettings(
        int maxReachDistance,
        int maxBlockPlacePerAxis,
        int maxBlockPlaceAtOnce,
        boolean canBreakFar,
        boolean enableUndoRedo,
        int undoStackSize
) {
    public DimensionSettings() {
        this(
                512,
                128,
                10_000,
                true,
                true,
                200
        );
    }

}
