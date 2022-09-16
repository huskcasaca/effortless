package dev.huskuraft.effortless.core;

public enum GameMode {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    public boolean isBlockPlacingRestricted() {
        return this == ADVENTURE || this == SPECTATOR;
    }

    public boolean isCreative() {
        return this == CREATIVE;
    }

    public boolean isSurvival() {
        return this == SURVIVAL || this == ADVENTURE;
    }

    public boolean isSpectator() {
        return this == SPECTATOR;
    }
}
