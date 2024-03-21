package dev.huskuraft.effortless.session.config;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import dev.huskuraft.effortless.api.core.Player;
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

    private  <T> T getEntry(Player player, Function<GeneralConfig, T> entry) {
        return entry.apply(playerConfigs.compute(player.getId(), (key, config) -> config == null || entry.apply(config) == null ? globalConfig : config));
    }

    public GeneralConfig getPlayerConfig(Player player) {
        return new GeneralConfig(
                getEntry(player, GeneralConfig::useCommands),
                getEntry(player, GeneralConfig::allowUseMod),
                getEntry(player, GeneralConfig::allowBreakBlocks),
                getEntry(player, GeneralConfig::allowPlaceBlocks),
                getEntry(player, GeneralConfig::maxReachDistance),
                getEntry(player, GeneralConfig::maxDistancePerAxis),
                getEntry(player, GeneralConfig::maxBreakBlocks),
                getEntry(player, GeneralConfig::maxPlaceBlocks),
                getEntry(player, GeneralConfig::whitelistedItems),
                getEntry(player, GeneralConfig::blacklistedItems)
        );
    }

}
