package dev.huskuraft.effortless.session.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import dev.huskuraft.effortless.building.config.ModConfig;

public record SessionConfig(
        GeneralConfig globalConfig,
        Map<UUID, GeneralConfig> playerConfigs
) implements ModConfig {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() {

    }

    public static SessionConfig defaultConfig() {
        return new SessionConfig(GeneralConfig.DEFAULT, Map.of());
    }

    private  <T> T getPlayerOrGlobalEntry(UUID id, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.compute(id, (key, config) -> config == null || entry.apply(config) == null ? globalConfig : config));
    }

    private  <T> T getPlayerOrNullEntry(UUID id, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.compute(id, (key, config) -> config == null || entry.apply(config) == null ? GeneralConfig.NULL : config));
    }

    public GeneralConfig getGlobalConfig() {
        return globalConfig;
    }

    public GeneralConfig getPlayerOrGlobalConfig(UUID id) {
        return new GeneralConfig(
                getPlayerOrGlobalEntry(id, GeneralConfig::useCommands),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowUseMod),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowBreakBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::allowPlaceBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxReachDistance),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxDistancePerAxis),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxBreakBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::maxPlaceBlocks),
                getPlayerOrGlobalEntry(id, GeneralConfig::whitelistedItems),
                getPlayerOrGlobalEntry(id, GeneralConfig::blacklistedItems)
        );
    }

    public GeneralConfig getPlayerOrNullConfig(UUID id) {
        return new GeneralConfig(
                getPlayerOrNullEntry(id, GeneralConfig::useCommands),
                getPlayerOrNullEntry(id, GeneralConfig::allowUseMod),
                getPlayerOrNullEntry(id, GeneralConfig::allowBreakBlocks),
                getPlayerOrNullEntry(id, GeneralConfig::allowPlaceBlocks),
                getPlayerOrNullEntry(id, GeneralConfig::maxReachDistance),
                getPlayerOrNullEntry(id, GeneralConfig::maxDistancePerAxis),
                getPlayerOrNullEntry(id, GeneralConfig::maxBreakBlocks),
                getPlayerOrNullEntry(id, GeneralConfig::maxPlaceBlocks),
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

    public SessionConfig withGlobalConfig(GeneralConfig config) {
        return new SessionConfig(
                config,
                playerConfigs
        );
    }

}
