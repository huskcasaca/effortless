package dev.huskuraft.effortless.session.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import dev.huskuraft.effortless.api.core.Player;

public record SessionConfig(
        GeneralConfig globalConfig,
        Map<UUID, GeneralConfig> playerConfigs
) {

    public static SessionConfig defaultConfig() {
        return new SessionConfig(GeneralConfig.DEFAULT, Map.of());
    }

    public static final SessionConfig EMPTY = new SessionConfig(GeneralConfig.EMPTY, Map.of());

    private  <T> T getPlayerOrGlobalEntry(UUID id, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.get(id) == null || entry.apply(playerConfigs.get(id)) == null ? globalConfig : playerConfigs.get(id));
    }

    private  <T> T getPlayerOrNullEntry(UUID id, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.get(id) == null || entry.apply(playerConfigs.get(id)) == null ? GeneralConfig.NULL : playerConfigs.get(id));
    }

    public GeneralConfig getGlobalConfig() {
        return globalConfig;
    }

    public GeneralConfig getPlayerConfig(Player player) {
        return getPlayerConfig(player.getId());
    }

    public GeneralConfig getPlayerConfig(UUID id) {
        return new GeneralConfig(
                getPlayerOrGlobalEntry(id, GeneralConfig::useCommands),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowUseMod),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowBreakBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowPlaceBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxReachDistance),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxDistancePerAxis),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxBreakBoxVolume),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxPlaceBoxVolume),
                getPlayerOrGlobalEntry(id, GeneralConfig::whitelistedItems),
                getPlayerOrGlobalEntry(id, GeneralConfig::blacklistedItems)
        );
    }

    public GeneralConfig getPlayerConfigOrNull(Player player) {
        return getPlayerConfigOrNull(player.getId());
    }

    public GeneralConfig getPlayerConfigOrNull(UUID id) {
        return new GeneralConfig(
                getPlayerOrNullEntry(id, GeneralConfig::useCommands),
                getPlayerOrNullEntry(id, GeneralConfig::allowUseMod),
                getPlayerOrNullEntry(id, GeneralConfig::allowBreakBlocks),
                getPlayerOrNullEntry(id, GeneralConfig::allowPlaceBlocks),
                getPlayerOrNullEntry(id, GeneralConfig::maxReachDistance),
                getPlayerOrNullEntry(id, GeneralConfig::maxDistancePerAxis),
                getPlayerOrNullEntry(id, GeneralConfig::maxBreakBoxVolume),
                getPlayerOrNullEntry(id, GeneralConfig::maxPlaceBoxVolume),
                getPlayerOrNullEntry(id, GeneralConfig::whitelistedItems),
                getPlayerOrNullEntry(id, GeneralConfig::blacklistedItems)
        );
    }

    public SessionConfig withPlayerConfig(UUID id, GeneralConfig config) {
        var map = new HashMap<>(playerConfigs);
        if (config == null) {
            map.remove(id);
        } else {
            map.put(id, config);
        }

        return new SessionConfig(
                globalConfig,
                Collections.unmodifiableMap(map)
        );
    }

    public SessionConfig withPlayerConfig(Map<UUID, GeneralConfig> config) {

        return new SessionConfig(
                globalConfig,
                config
        );
    }

    public SessionConfig withGlobalConfig(GeneralConfig config) {
        return new SessionConfig(
                config,
                playerConfigs
        );
    }

}
