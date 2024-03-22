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

    private  <T> T getEntry(UUID id, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.compute(id, (key, config) -> config == null || entry.apply(config) == null ? globalConfig : config));
    }

    public GeneralConfig getGlobalConfig() {
        return globalConfig;
    }

    public GeneralConfig getPlayerConfig(UUID id) {
        return new GeneralConfig(
                getEntry(id, GeneralConfig::useCommands),
                getEntry(id, GeneralConfig::allowUseMod),
                getEntry(id, GeneralConfig::allowBreakBlocks),
                getEntry(id, GeneralConfig::allowPlaceBlocks),
                getEntry(id, GeneralConfig::maxReachDistance),
                getEntry(id, GeneralConfig::maxDistancePerAxis),
                getEntry(id, GeneralConfig::maxBreakBlocks),
                getEntry(id, GeneralConfig::maxPlaceBlocks),
                getEntry(id, GeneralConfig::whitelistedItems),
                getEntry(id, GeneralConfig::blacklistedItems)
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
