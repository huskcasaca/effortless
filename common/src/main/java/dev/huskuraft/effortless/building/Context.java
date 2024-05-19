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
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.session.BatchBuildSession;
import dev.huskuraft.effortless.building.session.BuildSession;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public record Context(
        UUID id,
        BuildState buildState,
        BuildType buildType,
        BuildInteractions buildInteractions,

        StructureParams structureParams,
        PatternParams patternParams,
        CustomParams customParams
) {

    public boolean useCorrectTool() {
        return true;
    }

    public boolean useLegacyBlockPlace() {
        return false;
    }

    public static Context defaultSet() {
        return new Context(
                UUID.randomUUID(),
                BuildState.IDLE,
                BuildType.BUILD,
                BuildInteractions.EMPTY,
                new StructureParams(
                        BuildStructure.DISABLED,
                        ReplaceMode.DISABLED
                ),
                new PatternParams(
                        Pattern.DISABLED
                ),
                new CustomParams(
                        GeneralConfig.DEFAULT
                )
        );
    }

    private static BlockInteraction withPosition(BlockInteraction blockInteraction, BlockPosition blockPosition) {
        return new BlockInteraction(blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d()), blockInteraction.getDirection(), blockPosition, blockInteraction.isInside());
    }

    public UUID id() {
        return id;
    }

    public boolean isIdle() {
        return buildState.isIdle();
    }

    public boolean isDisabled() {
        return buildMode() == BuildMode.DISABLED;
    }

    public boolean isPreview() {
        return buildType() == BuildType.PREVIEW || buildType() == BuildType.PREVIEW_SOUND;
    }

    public boolean isPreviewSound() {
        return buildType() == BuildType.PREVIEW_SOUND;
    }

    public BuildMode buildMode() {
        return structureParams.buildStructure().getMode();
    }

    public BuildStructure buildStructure() {
        return structureParams.buildStructure();
    }

    public boolean isBuilding() {
        return buildMode() != BuildMode.DISABLED && buildState() != BuildState.IDLE;
    }

    public boolean isMissingHit() {
        return buildInteractions().isMissing();
    }

    public boolean skipRaytrace() {
        return false;
    }

    public boolean isFulfilled() {
        return isBuilding() && buildStructure().traceSize(this) == interactionsSize();
    }

    public int interactionsSize() {
        return buildInteractions.size();
    }

    public boolean isInteractionEmpty() {
        return buildInteractions.isEmpty();
    }

    public BlockPosition getPosition(int index) {
        return buildInteractions.get(index).getBlockPosition();
    }
    public List<BlockPosition> getPositions() {
        return buildInteractions.results().stream().map(BlockInteraction::getBlockPosition).toList();
    }
    public BlockInteraction getInteraction(int index) {
        return buildInteractions.get(index);
    }
    public List<BlockInteraction> getInteractions() {
        return buildInteractions.results();
    }

    public Set<BuildFeature> buildFeatures() {
        return structureParams.buildStructure().getFeatures();
    }

    public ReplaceMode replaceMode() {
        return structureParams.replaceMode();
    }

    public int axisLimitation() {
        return Integer.MAX_VALUE;
    }

    public int maxNextReachDistance() {
        return 1024;
    }

    public int maxReachDistance() {
        return customParams.generalConfig.maxReachDistance();
    }

    public Pattern pattern() {
        return patternParams.pattern();
    }

    public long patternSeed() {
        return patternParams.seed();
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
        return new Context(id, state, buildType, buildInteractions, structureParams, patternParams, customParams);
    }

    public Context withBuildType(BuildType type) {
        return new Context(id, buildState, type, buildInteractions, structureParams, patternParams, customParams);
    }

    public Context withNextInteraction(BlockInteraction interaction) {
        return new Context(id, buildState, buildType, buildInteractions.put(interaction), structureParams, patternParams, customParams);
    }

    public Context withEmptyInteractions() {
        return new Context(id, buildState, buildType, BuildInteractions.EMPTY, structureParams, patternParams, customParams);
    }

    public Context withBuildStructure(BuildStructure buildStructure) {
        return new Context(id, buildState, buildType, buildInteractions, structureParams.withBuildStructure(buildStructure), patternParams, customParams);
    }

    public Context withReplaceMode(ReplaceMode replaceMode) {
        return new Context(id, buildState, buildType, buildInteractions, structureParams.withReplaceMode(replaceMode), patternParams, customParams);
    }

    public Context withPattern(Pattern pattern) {
        return new Context(id, buildState, buildType, buildInteractions, structureParams, patternParams.withPattern(pattern), customParams);
    }

    public Context withRandomPatternSeed() {
        return new Context(id, buildState, buildType, buildInteractions, structureParams, patternParams.withRandomSeed(), customParams);
    }

    public Context withLimitedPatternProducer(boolean limitedProducer) {
        return new Context(id, buildState, buildType, buildInteractions, structureParams, patternParams.withLimitProducer(limitedProducer), customParams);
    }

    public Context finalize(Player player, BuildStage stage) {
        switch (stage) {
            case TICK -> {
                return withPattern(pattern().finalize(player, stage)).withRandomPatternSeed().withLimitedPatternProducer(player.getGameMode().isSurvival());
            }
            case UPDATE_CONTEXT -> {
            }
            case INTERACT -> {
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
                BuildInteractions.EMPTY,
                structureParams,
                patternParams,
                customParams
        );
    }

    public BuildSession createSession(World world, Player player) {
        return new BatchBuildSession(world, player, this);
    }

    // for build mode only
    @Nullable
    public BlockInteraction trace(Player player) {
        return buildStructure().trace(player, this);
    }

    // for build mode only
    public Stream<BlockInteraction> collectInteractions() {
        if (tracingResult().isSuccess()) {
            return buildStructure().collect(this).map(blockPosition -> withPosition(getInteraction(0), blockPosition));
        } else {
            return Stream.empty();
        }
    }

    public Context withReachParams(CustomParams customParams) {
        return new Context(id, buildState, buildType, buildInteractions, structureParams, patternParams, customParams);
    }

    public Context withGeneralConfig(GeneralConfig config) {
        // FIXME: 4/4/24 commands
        return withReachParams(new CustomParams(config));
    }

    public Vector3i getInteractionBox() {
        if (buildInteractions().isEmpty() || buildInteractions().isMissing()) {
            return Vector3i.ZERO;
        }
        return BoundingBox3d.fromLowerCornersOf(buildInteractions().results().stream().map(BlockInteraction::getBlockPosition).toArray(Vector3i[]::new)).getSize().toVector3i();
    }

    public int getBoxVolume() {
        return buildStructure().volume(this);
    }

    public int getMaxBoxVolume() {
        return switch (buildState()) {
            case IDLE -> 0;
            case PLACE_BLOCK, INTERACT_BLOCK -> customParams().generalConfig().maxBlockPlaceVolume();
            case BREAK_BLOCK -> customParams().generalConfig().maxBlockBreakVolume();
        };
    }

    public boolean isBoxVolumeInBounds() {
        return getBoxVolume() <= getMaxBoxVolume();
    }

    public boolean hasPermission() {
        return switch (buildState()) {
            case IDLE -> true;
            case PLACE_BLOCK, INTERACT_BLOCK -> customParams().generalConfig().allowPlaceBlocks();
            case BREAK_BLOCK -> customParams().generalConfig().allowBreakBlocks();
        };
    }

    public record BuildInteractions(
            List<BlockInteraction> results
    ) {

        public static final BuildInteractions EMPTY = new BuildInteractions(Collections.emptyList());

        public int size() {
            return results.size();
        }

        public boolean isEmpty() {
            return results.isEmpty();
        }

        public BlockInteraction get(int index) {
            return results.get(index);
        }

        public BuildInteractions put(BlockInteraction interaction) {
            return new BuildInteractions(Stream.concat(results.stream(), Stream.of(interaction)).toList());
        }

        public boolean isMissing() {
            return results.stream().anyMatch(result -> result == null || result.getTarget() != Interaction.Target.BLOCK);
        }

    }

    public record StructureParams(
            BuildStructure buildStructure,
            ReplaceMode replaceMode
    ) {

        public StructureParams withBuildStructure(BuildStructure buildStructure) {
            return new StructureParams(buildStructure, replaceMode);
        }
//
        public StructureParams withReplaceMode(ReplaceMode replaceMode) {
            return new StructureParams(buildStructure, replaceMode);
        }
    }

    public record PatternParams(
            Pattern pattern,
            boolean limitedProducer,
            long seed
    ) {

        public PatternParams(Pattern pattern) {
            this(pattern, false, new Random().nextLong());
        }

        public PatternParams withPattern(Pattern pattern) {
            return new PatternParams(pattern, limitedProducer, seed);
        }

        public PatternParams withRandomSeed() {
            return new PatternParams(pattern, limitedProducer, new Random().nextLong());
        }

        public PatternParams withLimitProducer(boolean limitedProducer) {
            return new PatternParams(pattern, limitedProducer, new Random().nextLong());
        }
    }

    public record CustomParams(
            GeneralConfig generalConfig
    ) {
    }


}
