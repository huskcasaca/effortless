package dev.huskuraft.effortless.building.clipboard;

import java.util.List;

public record Clipboard(
        boolean enabled,
        List<BlockSnapshot> blockSnapshots
) {
    public static Clipboard DISABLED = new Clipboard(false, List.of());

    public static Clipboard of(boolean enabled, List<BlockSnapshot> blockSnapshots) {
        return new Clipboard(enabled, blockSnapshots);
    }

    public Clipboard withEnabled(boolean enabled) {
        return new Clipboard(enabled, blockSnapshots);
    }

    public Clipboard withBlockSnapshots(List<BlockSnapshot> blockSnapshots) {
        return new Clipboard(enabled, blockSnapshots);
    }

}
