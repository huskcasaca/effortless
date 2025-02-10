package dev.huskuraft.effortless.session.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import dev.huskuraft.universal.api.core.Player;

public record SessionConfig(
        ConstraintConfig globalConfig,
        Map<UUID, ConstraintConfig> playerConfigs
) {

    public static final SessionConfig EMPTY = new SessionConfig(ConstraintConfig.EMPTY, Map.of());

    public static SessionConfig defaultConfig() {
        return new SessionConfig(ConstraintConfig.DEFAULT, Map.of());
    }

    private <T> T getPlayerOrGlobalEntry(UUID id, Function<ConstraintConfig, T> entry) {
        return entry.apply(playerConfigs.get(id) == null || entry.apply(playerConfigs.get(id)) == null ? globalConfig : playerConfigs.get(id));
    }

    private <T> T getPlayerOrNullEntry(UUID id, Function<ConstraintConfig, T> entry) {
        return entry.apply(playerConfigs.get(id) == null || entry.apply(playerConfigs.get(id)) == null ? ConstraintConfig.NULL : playerConfigs.get(id));
    }

    public ConstraintConfig getGlobalConfig() {
        return globalConfig;
    }

    public ConstraintConfig getByPlayer(Player player) {
        return getByPlayer(player.getId());
    }

    public ConstraintConfig getByPlayer(UUID id) {
        return new ConstraintConfig(
                getPlayerOrGlobalEntry(id, ConstraintConfig::useCommands),
                getPlayerOrGlobalEntry(id, ConstraintConfig::allowUseMod),
                getPlayerOrGlobalEntry(id, ConstraintConfig::allowBreakBlocks),
                getPlayerOrGlobalEntry(id, ConstraintConfig::allowPlaceBlocks),
                getPlayerOrGlobalEntry(id, ConstraintConfig::allowInteractBlocks),
                getPlayerOrGlobalEntry(id, ConstraintConfig::allowCopyPasteStructures),
                getPlayerOrGlobalEntry(id, ConstraintConfig::useProperToolsOnly),
                getPlayerOrGlobalEntry(id, ConstraintConfig::maxReachDistance),
                getPlayerOrGlobalEntry(id, ConstraintConfig::maxBlockBreakVolume),
                getPlayerOrGlobalEntry(id, ConstraintConfig::maxBlockPlaceVolume),
                getPlayerOrGlobalEntry(id, ConstraintConfig::maxBlockInteractVolume),
                getPlayerOrGlobalEntry(id, ConstraintConfig::maxStructureCopyPasteVolume),
                getPlayerOrGlobalEntry(id, ConstraintConfig::whitelistedItems),
                getPlayerOrGlobalEntry(id, ConstraintConfig::blacklistedItems)
        );
    }

    public ConstraintConfig getPlayerConfigOrNull(Player player) {
        return getPlayerConfigOrNull(player.getId());
    }

    public ConstraintConfig getPlayerConfigOrNull(UUID id) {
        return new ConstraintConfig(
                getPlayerOrNullEntry(id, ConstraintConfig::useCommands),
                getPlayerOrNullEntry(id, ConstraintConfig::allowUseMod),
                getPlayerOrNullEntry(id, ConstraintConfig::allowBreakBlocks),
                getPlayerOrNullEntry(id, ConstraintConfig::allowPlaceBlocks),
                getPlayerOrNullEntry(id, ConstraintConfig::allowInteractBlocks),
                getPlayerOrNullEntry(id, ConstraintConfig::allowCopyPasteStructures),
                getPlayerOrNullEntry(id, ConstraintConfig::useProperToolsOnly),
                getPlayerOrNullEntry(id, ConstraintConfig::maxReachDistance),
                getPlayerOrNullEntry(id, ConstraintConfig::maxBlockBreakVolume),
                getPlayerOrNullEntry(id, ConstraintConfig::maxBlockPlaceVolume),
                getPlayerOrNullEntry(id, ConstraintConfig::maxBlockInteractVolume),
                getPlayerOrNullEntry(id, ConstraintConfig::maxStructureCopyPasteVolume),
                getPlayerOrNullEntry(id, ConstraintConfig::whitelistedItems),
                getPlayerOrNullEntry(id, ConstraintConfig::blacklistedItems));
    }

    public SessionConfig withPlayerConfig(UUID id, ConstraintConfig config) {
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

    public SessionConfig withPlayerConfig(Map<UUID, ConstraintConfig> config) {

        return new SessionConfig(
                globalConfig,
                config
        );
    }

    public SessionConfig withGlobalConfig(ConstraintConfig config) {
        return new SessionConfig(
                config,
                playerConfigs
        );
    }

}
