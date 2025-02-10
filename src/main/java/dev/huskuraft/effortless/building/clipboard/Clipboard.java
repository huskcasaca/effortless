package dev.huskuraft.effortless.building.clipboard;

import java.util.List;

import dev.huskuraft.effortless.building.Option;

public record Clipboard(
        boolean enabled,
        Snapshot snapshot
) implements Option  {
    public static Clipboard DISABLED = new Clipboard(false, new Snapshot("", 0, List.of()));

    public static Clipboard of(boolean enabled, Snapshot snapshot) {
        return new Clipboard(enabled, snapshot);
    }

    public Clipboard withEnabled(boolean enabled) {
        return new Clipboard(enabled, snapshot);
    }

    public Clipboard withSnapshot(Snapshot snapshot) {
        return new Clipboard(enabled, snapshot);
    }

    public Clipboard toggled() {
        return new Clipboard(!enabled, snapshot);
    }

    public boolean isEmpty() {
        return snapshot.isEmpty();
    }

    public boolean copyAir() {
        return false;
    }

    @Override
    public String getName() {
        if (enabled) {
            return "clipboard_enabled";
        } else {
            return "clipboard_disabled";
        }
    }

    @Override
    public String getCategory() {
        return "clipboard";
    }
}
