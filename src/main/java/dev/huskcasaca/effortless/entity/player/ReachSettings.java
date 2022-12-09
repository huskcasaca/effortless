package dev.huskcasaca.effortless.entity.player;

public record ReachSettings(
        int maxReachDistance,
        int maxBlockPlacePerAxis,
        int maxBlockPlaceAtOnce,
        boolean canBreakFar,
        boolean enableUndoRedo,
        int undoStackSize
) {
    public ReachSettings() {
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
