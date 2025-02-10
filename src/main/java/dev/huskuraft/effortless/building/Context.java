package dev.huskuraft.effortless.building;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.GameMode;
import dev.huskuraft.universal.api.core.Interaction;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.Vector3i;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.building.config.BuilderConfig;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.Replace;
import dev.huskuraft.effortless.building.replace.ReplaceStrategy;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;
import dev.huskuraft.effortless.session.config.ConstraintConfig;

public record Context(
        UUID id,
        BuildState buildState,
        BuildType buildType,
        Interactions interactions,

        Structure structure,
        Clipboard clipboard,
        Pattern pattern,
        Replace replace,

        Configs configs,
        Extras extras
) {

    public boolean fillContainers() {
        return true;
    }

    public boolean useProperTool() {
        return configs().constraintConfig().useProperToolsOnly();
    }

    public int getReservedToolDurability() {
        return configs().builderConfig().reservedToolDurability();
    }

    public boolean useLegacyBlockPlace() {
        return false;
    }

    public static Context defaultSet() {
        return new Context(
                UUID.randomUUID(),
                BuildState.IDLE,
                BuildType.BUILD,
                Interactions.EMPTY,
                Structure.DISABLED,
                Clipboard.DISABLED,
                Pattern.DISABLED,
                Replace.DISABLED,
                new Configs(
                        ConstraintConfig.DEFAULT,
                        BuilderConfig.DEFAULT
                ), null
        );
    }

    public ReplaceStrategy replaceStrategy() {
        return replace.replaceStrategy();
    }

    private static BlockInteraction withPosition(BlockInteraction blockInteraction, BlockPosition blockPosition) {
        return new BlockInteraction(blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d()), blockInteraction.getDirection(), blockPosition, blockInteraction.isInside(), blockInteraction.isMiss());
    }

    public BuildMode buildMode() {
        return structure().getMode();
    }

    public boolean isIdle() {
        return buildState.isIdle();
    }

    public boolean isDisabled() {
        return buildMode() == BuildMode.DISABLED;
    }

    public boolean isBuildType() {
        return buildType() == BuildType.BUILD;
    }

    public boolean isBuildClientType() {
        return buildType() == BuildType.BUILD_CLIENT;
    }

    public boolean isPreviewType() {
        return buildType() == BuildType.PREVIEW;
    }

    public boolean isBuilding() {
        return buildMode() != BuildMode.DISABLED && buildState() != BuildState.IDLE;
    }

    public boolean isMissingHit() {
        return interactions().isMissing();
    }

    public boolean skipRaytrace() {
        return false;
    }

    public boolean isFulfilled() {
        return isBuilding() && ((buildState() == BuildState.PASTE_STRUCTURE && interactionsSize() == 1) || structure().traceSize(this) == interactionsSize());
    }

    public int interactionsSize() {
        return interactions.size();
    }

    public boolean isInteractionEmpty() {
        return interactions.isEmpty();
    }

    public BlockPosition getPosition(int index) {
        return interactions.get(index).getBlockPosition();
    }

    public List<BlockPosition> getPositions() {
        return interactions.results().stream().map(BlockInteraction::getBlockPosition).toList();
    }

    public BlockInteraction getInteraction(int index) {
        return interactions.get(index);
    }

    public List<BlockInteraction> getInteractions() {
        return interactions.results();
    }

    public Set<BuildFeature> buildFeatures() {
        return structure().getFeatures();
    }

    public int axisLimitation() {
        return Integer.MAX_VALUE;
    }

    public int maxNextReachDistance() {
        return 1024;
    }

    public int maxReachDistance() {
        return configs.constraintConfig.maxReachDistance();
    }

    public TracingResult tracingResult() {
        if (isIdle()) {
            return TracingResult.PASS;
        }
        if (isDisabled()) {
            return TracingResult.PASS;
        }
        if (isMissingHit()) {
            return TracingResult.FAILED;
        }

        if (isFulfilled()) {
            return TracingResult.SUCCESS_FULFILLED;
        } else {
            return TracingResult.SUCCESS_PARTIAL;
        }
    }

    public Context withBuildState(BuildState state) {
        return new Context(id, state, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withBuildType(BuildType type) {
        return new Context(id, buildState, type, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withNextInteraction(BlockInteraction interaction) {
        return new Context(id, buildState, buildType, interactions.put(interaction), structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withNoInteraction() {
        return new Context(id, buildState, buildType, Interactions.EMPTY, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withStructure(Structure structure) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withClipboard(Clipboard clipboard) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withEmptyClipboard() {
        return new Context(id, buildState, buildType, interactions, structure, clipboard.withSnapshot(Snapshot.EMPTY), pattern, replace, configs, extras);
    }

    public Context withPattern(Pattern pattern) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withReplace(Replace replace) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withExtras(Extras extras) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withPlayerExtras(Player player) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, new Extras(player));
    }

    public Context finalize(Player player, BuildStage stage) {
        switch (stage) {
            case TICK -> {
                return withPattern(pattern().finalize(player, stage)).withPlayerExtras(player);
            }
        }
        return withPattern(pattern().finalize(player, stage));
    }

    // new context for idle
    public Context newInteraction() {
        return new Context(
                UUID.randomUUID(),
                BuildState.IDLE,
                buildType,
                Interactions.EMPTY,
                structure,
                clipboard,
                pattern,
                replace,
                configs,
                extras
        );
    }

    // for build mode only
    @Nullable
    public BlockInteraction trace(Player player) {
        return structure().trace(player, this);
    }

    // for build mode only
    public Stream<BlockInteraction> collectInteractions() {
        if (tracingResult().isSuccess()) {
            return structure().collect(this).map(blockPosition -> withPosition(getInteraction(0), blockPosition));
        } else {
            return Stream.empty();
        }
    }

    public Context withReachParams(Configs configs) {
        return new Context(id, buildState, buildType, interactions, structure, clipboard, pattern, replace, configs, extras);
    }

    public Context withConstraintConfig(ConstraintConfig config) {
        // FIXME: 4/4/24 commands
        return withReachParams(new Configs(config, configs().builderConfig()));
    }

    public Context withBuilderConfig(BuilderConfig config) {
        // FIXME: 4/4/24 commands
        return withReachParams(new Configs(configs().constraintConfig(), config));
    }

    public Vector3i getInteractionBox() {
        if (buildState() == BuildState.PASTE_STRUCTURE) {
            return clipboard().snapshot().box();
        }
        if (interactions().isEmpty() || interactions().isMissing()) {
            return Vector3i.ZERO;
        }
        return BoundingBox3d.fromLowerCornersOf(interactions().results().stream().map(BlockInteraction::getBlockPosition).map(BlockPosition::toVector3i).toArray(Vector3i[]::new)).getSize().toVector3i();
    }

    public int getVolume() {
        if (buildState() == BuildState.PASTE_STRUCTURE) {
            return (int) (clipboard().snapshot().volume() * pattern().volumeMultiplier());
        }
        return (int) (structure().volume(this) * pattern().volumeMultiplier());
    }

    public int getMaxVolume() {
        return switch (buildState()) {
            case IDLE -> 0;
            case BREAK_BLOCK -> configs().constraintConfig().maxBlockBreakVolume();
            case PLACE_BLOCK -> configs().constraintConfig().maxBlockPlaceVolume();
            case INTERACT_BLOCK -> configs().constraintConfig().maxBlockInteractVolume();
            case COPY_STRUCTURE, PASTE_STRUCTURE -> configs().constraintConfig().maxStructureCopyPasteVolume();
        };
    }

    public boolean isVolumeInBounds() {
        return getVolume() <= getMaxVolume();
    }

    public boolean hasPermission() {
        return switch (buildState()) {
            case IDLE -> true;
            case BREAK_BLOCK -> configs().constraintConfig().allowBreakBlocks();
            case PLACE_BLOCK -> configs().constraintConfig().allowPlaceBlocks();
            case INTERACT_BLOCK -> configs().constraintConfig().allowInteractBlocks();
            case COPY_STRUCTURE, PASTE_STRUCTURE -> configs().constraintConfig().allowCopyPasteStructures();
        };
    }

    public record Interactions(
            List<BlockInteraction> results
    ) {

        public static final Interactions EMPTY = new Interactions(Collections.emptyList());

        public int size() {
            return results.size();
        }

        public boolean isEmpty() {
            return results.isEmpty();
        }

        public BlockInteraction get(int index) {
            return results.get(index);
        }

        public Interactions put(BlockInteraction interaction) {
            return new Interactions(Stream.concat(results.stream(), Stream.of(interaction)).toList());
        }

        public boolean isMissing() {
            return results.stream().anyMatch(result -> result == null || result.getTarget() != Interaction.Target.BLOCK);
        }

    }

    public record Extras(
            ResourceLocation dimensionId,
            dev.huskuraft.effortless.building.operation.block.Extras extras,
            GameMode gameMode,
            long seed,
            InventorySnapshot inventorySnapshot
    ) {

        public Extras(Player player) {
            this(player.getWorld().getDimensionId().location(), dev.huskuraft.effortless.building.operation.block.Extras.get(player), player.getGameMode(), new Random().nextLong(), new InventorySnapshot(player.getInventory()));
        }
    }

    public record Configs(
            ConstraintConfig constraintConfig,
            BuilderConfig builderConfig
    ) {
    }


}
