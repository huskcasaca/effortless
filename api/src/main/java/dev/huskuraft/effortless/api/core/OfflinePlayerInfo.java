package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.text.Text;

public record OfflinePlayerInfo(UUID id, String name, Text displayName) implements PlayerInfo {

    public OfflinePlayerInfo(UUID id) {
        this(id, id.toString(), Text.empty());
    }

    @Override
    public Object referenceValue() {
        return null;
    }
}
