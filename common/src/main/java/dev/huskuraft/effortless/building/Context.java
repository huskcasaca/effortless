package dev.huskuraft.effortless.building;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.GameMode;
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.building.operation.block.EntityState;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.session.BatchBuildSession;
import dev.huskuraft.effortless.building.session.BuildSession;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public record Context(
        UUID id,
        BuildState buildState,
        BuildType buildType,
        Interactions interactions,

        Structure structure,
        Pattern pattern,
        ReplaceMode replaceMode,

        Configs configs,
        Extras extras
) {

    public boolean useCorrectTool() {
        return configs().generalConfig().useCorrectTools();
    }

    public int getReservedToolDurability() {
        return 1;
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
                Pattern.DISABLED,
                ReplaceMode.DISABLED,
                new Configs(
                        GeneralConfig.DEFAULT
                ), null
        );
    }

    private static BlockInteraction withPosition(BlockInteraction blockInteraction, BlockPosition blockPosition) {
        return new BlockInteraction(blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d()), blockInteraction.getDirection(), blockPosition, blockInteraction.isInside());
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

    public boolean isPreviewType() {
        return buildType() == BuildType.PREVIEW || buildType() == BuildType.PREVIEW_ONCE;
    }

    public boolean isPreviewOnceType() {
        return buildType() == BuildType.PREVIEW_ONCE;
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
        return isBuilding() && structure().traceSize(this) == interactionsSize();
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
        return configs.generalConfig.maxReachDistance();
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
        return new Context(id, state, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withBuildType(BuildType type) {
        return new Context(id, buildState, type, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withNextInteraction(BlockInteraction interaction) {
        return new Context(id, buildState, buildType, interactions.put(interaction), structure, pattern, replaceMode, configs, extras);
    }

    public Context withNoInteraction() {
        return new Context(id, buildState, buildType, Interactions.EMPTY, structure, pattern, replaceMode, configs, extras);
    }

    public Context withStructure(Structure structure) {
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withPattern(Pattern pattern) {
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withReplaceMode(ReplaceMode replaceMode) {
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withExtras(Extras extras) {
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withPlayerExtras(Player player) {
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, new Extras(player));
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
                structure, pattern, replaceMode,
                configs, extras
        );
    }

    public BuildSession createSession(World world, Player player) {
        return new BatchBuildSession(player, this);
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
        return new Context(id, buildState, buildType, interactions, structure, pattern, replaceMode, configs, extras);
    }

    public Context withGeneralConfig(GeneralConfig config) {
        // FIXME: 4/4/24 commands
        return withReachParams(new Configs(config));
    }

    public Vector3i getInteractionBox() {
        if (interactions().isEmpty() || interactions().isMissing()) {
            return Vector3i.ZERO;
        }
        return BoundingBox3d.fromLowerCornersOf(interactions().results().stream().map(BlockInteraction::getBlockPosition).map(BlockPosition::toVector3i).toArray(Vector3i[]::new)).getSize().toVector3i();
    }

    public int getBoxVolume() {
        return structure().volume(this);
    }

    public int getMaxBoxVolume() {
        return switch (buildState()) {
            case IDLE -> 0;
            case BREAK_BLOCK -> configs().generalConfig().maxBlockBreakVolume();
            case PLACE_BLOCK -> configs().generalConfig().maxBlockPlaceVolume();
            case INTERACT_BLOCK -> configs().generalConfig().maxBlockPlaceVolume();
        };
    }

    public boolean isBoxVolumeInBounds() {
        return getBoxVolume() <= getMaxBoxVolume();
    }

    public boolean hasPermission() {
        return switch (buildState()) {
            case IDLE -> true;
            case BREAK_BLOCK -> configs().generalConfig().allowBreakBlocks();
            case PLACE_BLOCK -> configs().generalConfig().allowPlaceBlocks();
            case INTERACT_BLOCK -> configs().generalConfig().allowInteractBlocks();
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
            ResourceLocation world,
            EntityState entityState,
            GameMode gameMode,
            long seed,
            InventorySnapshot inventorySnapshot
    ) {

        public Extras(Player player) {
            this(player.getWorld().getDimensionId().location(), EntityState.get(player), player.getGameMode(), new Random().nextLong(), new InventorySnapshot(player.getInventory()));
        }
    }

    public record Configs(
            GeneralConfig generalConfig
    ) {
    }


}
