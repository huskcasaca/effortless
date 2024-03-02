package dev.huskuraft.effortless.session;

import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.config.ModConfig;

public record SessionConfig(
        BuilderConfig globalConfig,
        Map<UUID, BuilderConfig> playerConfigs
) implements ModConfig {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() {

    }

    public static SessionConfig defaultConfig() {
        return new SessionConfig(BuilderConfig.DEFAULT, Map.of());
    }


}
