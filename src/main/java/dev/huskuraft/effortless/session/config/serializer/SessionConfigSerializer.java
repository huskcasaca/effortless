package dev.huskuraft.effortless.session.config.serializer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.session.config.ConstraintConfig;
import dev.huskuraft.effortless.session.config.SessionConfig;
import dev.huskuraft.universal.api.config.ConfigSerializer;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.nightconfig.core.CommentedConfig;
import dev.huskuraft.universal.api.nightconfig.core.Config;
import dev.huskuraft.universal.api.nightconfig.core.ConfigSpec;

public class SessionConfigSerializer implements ConfigSerializer<SessionConfig> {

    private static final String KEY_GLOBAL = "global";
    private static final String KEY_PLAYER = "player";

    private static final String KEY_USE_COMMANDS = "useCommands";
    private static final String KEY_ALLOW_USE_MOD = "allowUseMod";
    private static final String KEY_ALLOW_BREAK_BLOCKS = "allowBreakBlocks";
    private static final String KEY_ALLOW_PLACE_BLOCKS = "allowPlaceBlocks";
    private static final String KEY_ALLOW_INTERACT_BLOCKS = "allowInteractBlocks";
    private static final String KEY_ALLOW_COPY_PASTE_STRUCTURE = "allowCopyPasteStructure";
    private static final String KEY_USE_PROPER_TOOLS_ONLY = "useProperToolsOnly";
    private static final String KEY_MAX_REACH_DISTANCE = "maxReachDistance";
    private static final String KEY_MAX_BLOCK_BREAK_VOLUME = "maxBlockBreakVolume";
    private static final String KEY_MAX_BLOCK_PLACE_VOLUME = "maxBlockPlaceVolume";
    private static final String KEY_MAX_BLOCK_INTERACT_VOLUME = "maxBlockInteractVolume";
    private static final String KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME = "maxStructureCopyPasteVolume";
    private static final String KEY_WHITELISTED_ITEMS = "whitelistedItems";
    private static final String KEY_BLACKLISTED_ITEMS = "blacklistedItems";

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(KEY_GLOBAL, () -> GlobalConstraintConfigSerializer.INSTANCE.serialize(getDefault().globalConfig()), Config.class::isInstance);
        spec.define(KEY_PLAYER, () -> Config.inMemory(), Config.class::isInstance);
        return spec;
    }

    @Override
    public SessionConfig getDefault() {
        return SessionConfig.defaultConfig();
    }

    @Override
    public SessionConfig deserialize(Config config) {
        validate(config);
        return new SessionConfig(
                GlobalConstraintConfigSerializer.INSTANCE.deserialize(config.get(KEY_GLOBAL)),
                ((Config) config.get(KEY_PLAYER)).entrySet().stream().map(e -> {
                    try {
                        return Map.entry(UUID.fromString(e.getKey()), PlayerConstraintConfigSerializer.INSTANCE.deserialize(e.getValue()));
                    } catch (IllegalArgumentException | NullPointerException ignored) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
        );
    }

    @Override
    public Config serialize(SessionConfig sessionConfig) {
        var config = CommentedConfig.inMemory();
        config.add(KEY_GLOBAL, GlobalConstraintConfigSerializer.INSTANCE.serialize(sessionConfig.globalConfig()));
        for (var entry : sessionConfig.playerConfigs().entrySet()) {
            config.add(List.of(KEY_PLAYER, entry.getKey().toString()), PlayerConstraintConfigSerializer.INSTANCE.serialize(entry.getValue()));
        }
        validate(config);
        return config;
    }

    public static class GlobalConstraintConfigSerializer implements ConfigSerializer<ConstraintConfig> {

        public static final GlobalConstraintConfigSerializer INSTANCE = new GlobalConstraintConfigSerializer();

        private GlobalConstraintConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_USE_COMMANDS, () -> ConstraintConfig.USE_COMMANDS_DEFAULT, Objects::nonNull);
            spec.define(KEY_ALLOW_USE_MOD, () -> ConstraintConfig.ALLOW_USE_MOD_DEFAULT, Objects::nonNull);
            spec.define(KEY_ALLOW_BREAK_BLOCKS, () -> ConstraintConfig.ALLOW_BREAK_BLOCKS_DEFAULT, Objects::nonNull);
            spec.define(KEY_ALLOW_PLACE_BLOCKS, () -> ConstraintConfig.ALLOW_PLACE_BLOCKS_DEFAULT, Objects::nonNull);
            spec.define(KEY_ALLOW_INTERACT_BLOCKS, () -> ConstraintConfig.ALLOW_INTERACT_BLOCKS_DEFAULT, Objects::nonNull);
            spec.define(KEY_ALLOW_COPY_PASTE_STRUCTURE, () -> ConstraintConfig.ALLOW_COPY_PASTE_STRUCTURES_DEFAULT, Objects::nonNull);
            spec.define(KEY_USE_PROPER_TOOLS_ONLY, () -> ConstraintConfig.USE_PROPER_TOOLS_ONLY_DEFAULT, Objects::nonNull);
            spec.defineInRange(KEY_MAX_REACH_DISTANCE, ConstraintConfig.MAX_REACH_DISTANCE_DEFAULT, ConstraintConfig.MAX_REACH_DISTANCE_RANGE_START, ConstraintConfig.MAX_REACH_DISTANCE_RANGE_END);
            spec.defineInRange(KEY_MAX_BLOCK_BREAK_VOLUME, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_END);
            spec.defineInRange(KEY_MAX_BLOCK_PLACE_VOLUME, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_END);
            spec.defineInRange(KEY_MAX_BLOCK_INTERACT_VOLUME, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_END);
            spec.defineInRange(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_DEFAULT, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_START, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_END);
            spec.defineList(KEY_WHITELISTED_ITEMS, () -> ConstraintConfig.WHITELISTED_ITEMS_DEFAULT.stream().map(ResourceLocation::getString).toList(), Objects::nonNull);
            spec.defineList(KEY_BLACKLISTED_ITEMS, () -> ConstraintConfig.BLACKLISTED_ITEMS_DEFAULT.stream().map(ResourceLocation::getString).toList(), Objects::nonNull);
            return spec;
        }

        @Override
        public ConstraintConfig getDefault() {
            return ConstraintConfig.DEFAULT;
        }

        @Override
        public ConstraintConfig deserialize(Config config) {
            validate(config);
            return new ConstraintConfig(
                    config.get(KEY_USE_COMMANDS),
                    config.get(KEY_ALLOW_USE_MOD),
                    config.get(KEY_ALLOW_BREAK_BLOCKS),
                    config.get(KEY_ALLOW_PLACE_BLOCKS),
                    config.get(KEY_ALLOW_INTERACT_BLOCKS),
                    config.get(KEY_ALLOW_COPY_PASTE_STRUCTURE),
                    config.get(KEY_USE_PROPER_TOOLS_ONLY),
                    config.get(KEY_MAX_REACH_DISTANCE),
                    config.get(KEY_MAX_BLOCK_BREAK_VOLUME),
                    config.get(KEY_MAX_BLOCK_PLACE_VOLUME),
                    config.get(KEY_MAX_BLOCK_INTERACT_VOLUME),
                    config.get(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME),
                    config.get(KEY_WHITELISTED_ITEMS) == null ? null : config.<List<String>>get(KEY_WHITELISTED_ITEMS).stream().map(ResourceLocation::decompose).toList(),
                    config.get(KEY_BLACKLISTED_ITEMS) == null ? null : config.<List<String>>get(KEY_BLACKLISTED_ITEMS).stream().map(ResourceLocation::decompose).toList()
            );
        }

        @Override
        public Config serialize(ConstraintConfig constraintConfig) {
            var config = CommentedConfig.inMemory();
            config.set(KEY_USE_COMMANDS, constraintConfig.useCommands());
            config.set(KEY_ALLOW_USE_MOD, constraintConfig.allowUseMod());
            config.set(KEY_ALLOW_BREAK_BLOCKS, constraintConfig.allowBreakBlocks());
            config.set(KEY_ALLOW_PLACE_BLOCKS, constraintConfig.allowPlaceBlocks());
            config.set(KEY_ALLOW_INTERACT_BLOCKS, constraintConfig.allowInteractBlocks());
            config.set(KEY_ALLOW_COPY_PASTE_STRUCTURE, constraintConfig.allowCopyPasteStructures());
            config.set(KEY_USE_PROPER_TOOLS_ONLY, constraintConfig.useProperToolsOnly());
            config.set(KEY_MAX_REACH_DISTANCE, constraintConfig.maxReachDistance());
            config.set(KEY_MAX_BLOCK_BREAK_VOLUME, constraintConfig.maxBlockBreakVolume());
            config.set(KEY_MAX_BLOCK_PLACE_VOLUME, constraintConfig.maxBlockPlaceVolume());
            config.set(KEY_MAX_BLOCK_INTERACT_VOLUME, constraintConfig.maxBlockInteractVolume());
            config.set(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME, constraintConfig.maxStructureCopyPasteVolume());
            config.set(KEY_WHITELISTED_ITEMS, constraintConfig.whitelistedItems() == null ? null : constraintConfig.whitelistedItems().stream().map(ResourceLocation::getString).toList());
            config.set(KEY_BLACKLISTED_ITEMS, constraintConfig.blacklistedItems() == null ? null : constraintConfig.blacklistedItems().stream().map(ResourceLocation::getString).toList());

            config.setComment(KEY_USE_COMMANDS, "Should use commands to build using this mod.");
            config.setComment(KEY_ALLOW_USE_MOD, "Should allow players to use this mod.");
            config.setComment(KEY_ALLOW_BREAK_BLOCKS, "Should allow players to break blocks using this mod.");
            config.setComment(KEY_ALLOW_PLACE_BLOCKS, "Should allow players to place blocks using this mod.");
            config.setComment(KEY_ALLOW_INTERACT_BLOCKS, "Should allow players to interact blocks using this mod.");
            config.setComment(KEY_ALLOW_COPY_PASTE_STRUCTURE, "Should allow players to copy and paste structures using this mod.");
            config.setComment(KEY_USE_PROPER_TOOLS_ONLY, "Should allow players to break blocks with proper tools only in survival mode.");
            config.setComment(KEY_MAX_REACH_DISTANCE, "The maximum distance a player can reach when building using this mod. \nRange: " + ConstraintConfig.MAX_REACH_DISTANCE_RANGE_START + " ~ " + ConstraintConfig.MAX_REACH_DISTANCE_RANGE_END);
            config.setComment(KEY_MAX_BLOCK_BREAK_VOLUME, "The maximum block volume a player can break at once when building using this mod. \nRange: " + ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_START + " ~ " + ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_END);
            config.setComment(KEY_MAX_BLOCK_PLACE_VOLUME, "The maximum block volume a player can place at once when building using this mod. \nRange: " + ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_START + " ~ " + ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_END);
            config.setComment(KEY_MAX_BLOCK_INTERACT_VOLUME, "The maximum block volume a player can interact at once when building using this mod. \nRange: " + ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_START + " ~ " + ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_END);
            config.setComment(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME, "The maximum structure volume a player can copy and paste at once when building using this mod. \nRange: " + ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_START + " ~ " + ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_END);
            config.setComment(KEY_WHITELISTED_ITEMS, "The list of items that players are allowed to break/place/interact when building using this mod. \nIf the whitelist is empty, all items are allowed. \nIf the whitelist is not empty, only the items in the whitelist are allowed. \nThe value must be a list of item resource locations like [\"minecraft:stone\", \"minecraft:dirt\"].");
            config.setComment(KEY_BLACKLISTED_ITEMS, "The list of items that players are not allowed to break/place/interact when building using this mod. \nIf the blacklist is empty, no items are not allowed. \nIf an item exists both in the blacklist and the whitelist, it will not be allowed. \nThe value must be a list of item resource locations like [\"minecraft:stone\", \"minecraft:dirt\"].");
            validate(config);
            return config;
        }

    }

    public static class PlayerConstraintConfigSerializer implements ConfigSerializer<ConstraintConfig> {

        public static final PlayerConstraintConfigSerializer INSTANCE = new PlayerConstraintConfigSerializer();

        private PlayerConstraintConfigSerializer() {
        }

        private static <T> void addOrRemove(Config config, String path, T value) {
            if (value == null) {
                config.remove(path);
            } else {
                config.add(path, value);
            }
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            if (config.contains(KEY_USE_COMMANDS)) {
                spec.define(KEY_USE_COMMANDS, () -> ConstraintConfig.USE_COMMANDS_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_ALLOW_USE_MOD)) {
                spec.define(KEY_ALLOW_USE_MOD, () -> ConstraintConfig.ALLOW_USE_MOD_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_ALLOW_BREAK_BLOCKS)) {
                spec.define(KEY_ALLOW_BREAK_BLOCKS, () -> ConstraintConfig.ALLOW_BREAK_BLOCKS_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_ALLOW_PLACE_BLOCKS)) {
                spec.define(KEY_ALLOW_PLACE_BLOCKS, () -> ConstraintConfig.ALLOW_PLACE_BLOCKS_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_ALLOW_INTERACT_BLOCKS)) {
                spec.define(KEY_ALLOW_INTERACT_BLOCKS, () -> ConstraintConfig.ALLOW_INTERACT_BLOCKS_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_ALLOW_COPY_PASTE_STRUCTURE)) {
                spec.define(KEY_ALLOW_COPY_PASTE_STRUCTURE, () -> ConstraintConfig.ALLOW_COPY_PASTE_STRUCTURES_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_USE_PROPER_TOOLS_ONLY)) {
                spec.define(KEY_USE_PROPER_TOOLS_ONLY, () -> ConstraintConfig.USE_PROPER_TOOLS_ONLY_DEFAULT, Boolean.class::isInstance);
            }
            if (config.contains(KEY_MAX_REACH_DISTANCE)) {
                spec.defineInRange(KEY_MAX_REACH_DISTANCE, ConstraintConfig.MAX_REACH_DISTANCE_DEFAULT, ConstraintConfig.MAX_REACH_DISTANCE_RANGE_START, ConstraintConfig.MAX_REACH_DISTANCE_RANGE_END);
            }
            if (config.contains(KEY_MAX_BLOCK_BREAK_VOLUME)) {
                spec.defineInRange(KEY_MAX_BLOCK_BREAK_VOLUME, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_END);
            }
            if (config.contains(KEY_MAX_BLOCK_PLACE_VOLUME)) {
                spec.defineInRange(KEY_MAX_BLOCK_PLACE_VOLUME, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_END);
            }
            if (config.contains(KEY_MAX_BLOCK_INTERACT_VOLUME)) {
                spec.defineInRange(KEY_MAX_BLOCK_INTERACT_VOLUME, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_DEFAULT, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_END);
            }
            if (config.contains(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME)) {
                spec.defineInRange(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_DEFAULT, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_START, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_END);
            }
            if (config.contains(KEY_WHITELISTED_ITEMS)) {
                spec.defineList(KEY_WHITELISTED_ITEMS, () -> ConstraintConfig.WHITELISTED_ITEMS_DEFAULT, Objects::nonNull);
            }
            if (config.contains(KEY_BLACKLISTED_ITEMS)) {
                spec.defineList(KEY_BLACKLISTED_ITEMS, () -> ConstraintConfig.BLACKLISTED_ITEMS_DEFAULT, Objects::nonNull);
            }
            return spec;
        }

        @Override
        public ConstraintConfig getDefault() {
            return ConstraintConfig.NULL;
        }

        @Override
        public ConstraintConfig deserialize(Config config) {
            validate(config);
            return new ConstraintConfig(
                    config.get(KEY_USE_COMMANDS),
                    config.get(KEY_ALLOW_USE_MOD),
                    config.get(KEY_ALLOW_BREAK_BLOCKS),
                    config.get(KEY_ALLOW_PLACE_BLOCKS),
                    config.get(KEY_ALLOW_INTERACT_BLOCKS),
                    config.get(KEY_ALLOW_COPY_PASTE_STRUCTURE),
                    config.get(KEY_USE_PROPER_TOOLS_ONLY),
                    config.get(KEY_MAX_REACH_DISTANCE),
                    config.get(KEY_MAX_BLOCK_BREAK_VOLUME),
                    config.get(KEY_MAX_BLOCK_PLACE_VOLUME),
                    config.get(KEY_MAX_BLOCK_INTERACT_VOLUME),
                    config.get(KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME),
                    config.get(KEY_WHITELISTED_ITEMS) == null ? null : config.<List<String>>get(KEY_WHITELISTED_ITEMS).stream().map(ResourceLocation::decompose).toList(),
                    config.get(KEY_BLACKLISTED_ITEMS) == null ? null : config.<List<String>>get(KEY_BLACKLISTED_ITEMS).stream().map(ResourceLocation::decompose).toList());
        }

        @Override
        public Config serialize(ConstraintConfig constraintConfig) {
            var config = CommentedConfig.inMemory();
            addOrRemove(config, KEY_USE_COMMANDS, constraintConfig.useCommands());
            addOrRemove(config, KEY_ALLOW_USE_MOD, constraintConfig.allowUseMod());
            addOrRemove(config, KEY_ALLOW_BREAK_BLOCKS, constraintConfig.allowBreakBlocks());
            addOrRemove(config, KEY_ALLOW_PLACE_BLOCKS, constraintConfig.allowPlaceBlocks());
            addOrRemove(config, KEY_ALLOW_INTERACT_BLOCKS, constraintConfig.allowInteractBlocks());
            addOrRemove(config, KEY_ALLOW_COPY_PASTE_STRUCTURE, constraintConfig.allowCopyPasteStructures());
            addOrRemove(config, KEY_USE_PROPER_TOOLS_ONLY, constraintConfig.useProperToolsOnly());
            addOrRemove(config, KEY_MAX_REACH_DISTANCE, constraintConfig.maxReachDistance());
            addOrRemove(config, KEY_MAX_BLOCK_BREAK_VOLUME, constraintConfig.maxBlockBreakVolume());
            addOrRemove(config, KEY_MAX_BLOCK_PLACE_VOLUME, constraintConfig.maxBlockPlaceVolume());
            addOrRemove(config, KEY_MAX_BLOCK_INTERACT_VOLUME, constraintConfig.maxBlockInteractVolume());
            addOrRemove(config, KEY_MAX_STRUCTURE_COPY_PASTE_VOLUME, constraintConfig.maxStructureCopyPasteVolume());
            addOrRemove(config, KEY_WHITELISTED_ITEMS, constraintConfig.whitelistedItems() == null ? null : constraintConfig.whitelistedItems().stream().map(ResourceLocation::getString).toList());
            addOrRemove(config, KEY_BLACKLISTED_ITEMS, constraintConfig.blacklistedItems() == null ? null : constraintConfig.blacklistedItems().stream().map(ResourceLocation::getString).toList());
            validate(config);
            return config;
        }

    }


}
