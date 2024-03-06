package dev.huskuraft.effortless.session.config;

import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.building.config.ModConfig;

public record SessionConfig(
        BuildingConfig globalConfig,
        Map<UUID, BuildingConfig> playerConfigs
) implements ModConfig {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() {

    }

    public static SessionConfig defaultConfig() {
        return new SessionConfig(BuildingConfig.DEFAULT, Map.of());
    }


}
