package dev.huskuraft.effortless.api.core;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.api.text.Text;

public record OfflinePlayerInfo(UUID id, String name, Text displayName, PlayerSkin skin) implements PlayerInfo {

    public OfflinePlayerInfo(UUID id) {
        this(id, "Offline Player", Text.empty(), null);
    }

    @Override
    public PlayerProfile getProfile() {
        return new PlayerProfile() {
            @Override
            public UUID getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Map<String, ? extends Collection<?>> getProperties() {
                return null;
            }

            @Override
            public Object referenceValue() {
                throw new NullPointerException();
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return displayName;
    }

    @Override
    public PlayerSkin getSkin() {
        return skin;
    }

    @Override
    public Object referenceValue() {
        throw new NullPointerException();
    }

}
