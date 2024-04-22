package dev.huskuraft.effortless.building;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.CubeFilling;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.UniformLength;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public record Context(
        UUID getId,
        BuildState state,
        BuildType type,
        BuildInteractions interactions,

        StructureParams structureParams,
        PatternParams patternParams,
        ConfigParams limitationParams
) {

    public UUID getId() {
        return getId;
    }

    public static Context defaultSet() {
        return new Context(
                UUID.randomUUID(),
                BuildState.IDLE,
                BuildType.BUILD,
                BuildInteractions.EMPTY,
                new StructureParams(
                        BuildMode.DISABLED,
                        CircleStart.CIRCLE_START_CORNER,
                        CubeFilling.CUBE_FULL,
                        PlaneFilling.PLANE_FULL,
                        PlaneFacing.BOTH,
                        RaisedEdge.RAISE_LONG_EDGE,
                        ReplaceMode.DISABLED,
                        UniformLength.DISABLE
                ),
                new PatternParams(
                        Pattern.DISABLED
                ),
                new ConfigParams(
                        GeneralConfig.DEFAULT
                )
        );
    }

    private static BlockInteraction withPosition(BlockInteraction blockInteraction, BlockPosition blockPosition) {
        return new BlockInteraction(blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d()), blockInteraction.getDirection(), blockPosition, blockInteraction.isInside());
    }

    public boolean isIdle() {
        return state.isIdle();
    }

    public boolean isPlacingBlock() {
        return state() == BuildState.PLACE_BLOCK;
    }

    public boolean isBreakingBlock() {
        return state() == BuildState.BREAK_BLOCK;
    }

    public boolean isDisabled() {
        return structureParams().buildMode() == BuildMode.DISABLED;
    }

    public boolean isPreview() {
        return type() == BuildType.PREVIEW || type() == BuildType.PREVIEW_ONCE;
    }

    public boolean isPreviewOnce() {
        return type() == BuildType.PREVIEW_ONCE;
    }

    public BuildMode buildMode() {
        return structureParams.buildMode();
    }

    public boolean isBuilding() {
        return buildMode() != BuildMode.DISABLED && state() != BuildState.IDLE;
    }

    public boolean isMissingHit() {
        return interactions().isMissing();
    }

    public boolean skipRaytrace() {
        return false;
    }

    public boolean isFulfilled() {
        return isBuilding() && structureParams().buildMode().getInstance().totalClicks(this) == interactionsSize();
    }

    public int interactionsSize() {
        return interactions.size();
    }

    public boolean isInteractionEmpty() {
        return interactions.isEmpty();
    }

    public BlockInteraction firstBlockInteraction() {
        return interactions.get(0);
    }

    public BlockInteraction secondBlockInteraction() {
        return interactions.get(1);
    }

    public BlockInteraction thirdBlockInteraction() {
        return interactions.get(2);
    }

    public BlockPosition firstBlockPosition() {
        return firstBlockInteraction().getBlockPosition();
    }

    public BlockPosition secondBlockPosition() {
        return secondBlockInteraction().getBlockPosition();
    }

    public BlockPosition thirdBlockPosition() {
        return thirdBlockInteraction().getBlockPosition();
    }

    public Set<Feature> buildFeatures() {
        return structureParams.buildFeatures();
    }

    public CircleStart circleStart() {
        return structureParams.circleStart();
    }

    public CubeFilling cubeFilling() {
        return structureParams.cubeFilling();
    }

    public PlaneFilling planeFilling() {
        return structureParams.planeFilling();
    }

    public PlaneFacing planeFacing() {
        return structureParams.planeFacing();
    }

    public RaisedEdge raisedEdge() {
        return structureParams.raisedEdge();
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
        return limitationParams.generalConfig.maxReachDistance();
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

    public Context withPlacingState() {
        return this.withState(BuildState.PLACE_BLOCK);
    }

    public Context withBreakingState() {
        return this.withState(BuildState.BREAK_BLOCK);
    }

    public Context withState(BuildState state) {
        return new Context(getId, state, type, interactions, structureParams, patternParams, limitationParams);
    }

    public Context withPreviewType() {
        return new Context(getId, state, BuildType.PREVIEW, interactions, structureParams, patternParams, limitationParams);
    }

    public Context withPreviewOnceType() {
        return new Context(getId, state, BuildType.PREVIEW_ONCE, interactions, structureParams, patternParams, limitationParams);
    }

    public Context withNextInteraction(BlockInteraction interaction) {
        return new Context(getId, state, type, interactions.put(interaction), structureParams, patternParams, limitationParams);
    }

    public Context withEmptyInteractions() {
        return new Context(getId, state, type, BuildInteractions.EMPTY, structureParams, patternParams, limitationParams);
    }

    public Context withNextInteractionTraced(Player player) {
        return withNextInteraction(trace(player));
    }

    public Context withBuildMode(BuildMode buildMode) {
        return new Context(getId, state, type, interactions, structureParams.withBuildMode(buildMode), patternParams, limitationParams);
    }

    public Context withBuildFeature(Feature feature) {
        return new Context(getId, state, type, interactions, structureParams.withBuildFeature(feature), patternParams, limitationParams);
    }

    public Context withBuildFeature(Set<Feature> feature) {
        return new Context(getId, state, type, interactions, structureParams.withBuildFeature(feature), patternParams, limitationParams);
    }

    public Context withPattern(Pattern pattern) {
        return new Context(getId, state, type, interactions, structureParams, patternParams.withPattern(pattern), limitationParams);
    }

    public Context withRandomPatternSeed() {
        return new Context(getId, state, type, interactions, structureParams, patternParams.withRandomSeed(), limitationParams);
    }

    public Context finalize(Player player, BuildStage stage) {
        return withPattern(pattern().finalize(new BatchBuildSession(player.getWorld(), player, this), stage));
    }

    // new context for idle
    public Context newInteraction() {
        return new Context(
                UUID.randomUUID(),
                BuildState.IDLE,
                type,
                BuildInteractions.EMPTY,
                structureParams,
                patternParams,
                limitationParams
        );
    }

    public BuildSession createSession(World world, Player player) {
        return new BatchBuildSession(world, player, this);
    }

    // for build mode only
    @Nullable
    public BlockInteraction trace(Player player) {
        return buildMode().getInstance().trace(player, this);
    }

    // for build mode only
    public Stream<BlockInteraction> collect() {
        if (tracingResult().isSuccess()) {
            return buildMode().getInstance().collect(this).map(blockPosition -> withPosition(firstBlockInteraction(), blockPosition));
        } else {
            return Stream.empty();
        }
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
            BuildMode buildMode,
            CircleStart circleStart,
            CubeFilling cubeFilling,
            PlaneFilling planeFilling,
            PlaneFacing planeFacing,
            RaisedEdge raisedEdge,
            ReplaceMode replaceMode,
            UniformLength uniformLength
    ) {

        public Set<Feature> buildFeatures() {
            return Stream.of(
                    Set.of(circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge)
            ).flatMap(Set::stream).collect(Collectors.toSet());
        }

        public StructureParams withBuildMode(BuildMode buildMode) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withBuildFeature(Feature feature) {
            if (feature instanceof CircleStart feature1) {
                return withCircleStart(feature1);
            }
            if (feature instanceof CubeFilling feature1) {
                return withCubeFilling(feature1);
            }
            if (feature instanceof PlaneFilling feature1) {
                return withPlaneFilling(feature1);
            }
            if (feature instanceof PlaneFacing feature1) {
                return withPlaneFacing(feature1);
            }
            if (feature instanceof RaisedEdge feature1) {
                return withRaisedEdge(feature1);
            }
            if (feature instanceof ReplaceMode replaceMode1) {
                return withReplaceMode(replaceMode1);
            }
            return this;
        }

        public StructureParams withBuildFeature(Set<Feature> feature) {
//            if (feature.iterator().next() instanceof BuildFeature.PlaneFacing) {
//                return withPlaneFacing(feature.stream().map((o) -> (BuildFeature.PlaneFacing)o).collect(Collectors.toCollection(() -> EnumSet.noneOf(BuildFeature.PlaneFacing.class))));
//            }
            return this;
        }

        public StructureParams withCircleStart(CircleStart circleStart) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withCubeFilling(CubeFilling cubeFilling) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withPlaneFilling(PlaneFilling planeFilling) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withPlaneFacing(PlaneFacing planeFacing) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withRaisedEdge(RaisedEdge raisedEdge) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withReplaceMode(ReplaceMode replaceMode) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

        public StructureParams withUniformLength(UniformLength uniformLength) {
            return new StructureParams(buildMode, circleStart, cubeFilling, planeFilling, planeFacing, raisedEdge, replaceMode, uniformLength);
        }

    }

    public record PatternParams(
            Pattern pattern,
            long seed
    ) {

        public PatternParams(Pattern pattern) {
            this(pattern, new Random().nextLong());
        }

        public PatternParams withPattern(Pattern pattern) {
            return new PatternParams(pattern, seed);
        }

        public PatternParams withRandomSeed() {
            return new PatternParams(pattern, new Random().nextLong());
        }
    }

    public record ConfigParams(
            GeneralConfig generalConfig
    ) {
    }


    public Context withReachParams(ConfigParams configParams) {
        return new Context(getId, state, type, interactions, structureParams, patternParams, configParams);
    }

    public Context withGeneralConfig(GeneralConfig config) {
        // FIXME: 4/4/24 commands
        return withReachParams(new ConfigParams(config));
    }

    public Vector3i getInteractionBox() {
        if (interactions().isEmpty() || interactions().isMissing()) {
            return Vector3i.ZERO;
        }
        return BoundingBox3d.fromLowerCornersOf(interactions().results().stream().map(BlockInteraction::getBlockPosition).toArray(Vector3i[]::new)).getSize().toVector3i();
    }

    public int getBoxVolume() {
        return getInteractionBox().volume();
    }

    public int getMaxBoxVolume() {
        return switch (state()) {
            case IDLE -> 0;
            case PLACE_BLOCK -> limitationParams().generalConfig().maxBoxVolumePerPlace();
            case BREAK_BLOCK -> limitationParams().generalConfig().maxBoxVolumePerBreak();
        };
    }

    public int getBoxSideLength() {
        return getInteractionBox().stream().max().orElse(0);
    }

    public int getMaxBoxSideLength() {
        return switch (state()) {
            case IDLE -> 0;
            case PLACE_BLOCK -> limitationParams().generalConfig().maxBoxSideLengthPerPlace();
            case BREAK_BLOCK -> limitationParams().generalConfig().maxBoxSideLengthPerBreak();
        };
    }

    public boolean isBoxVolumeInBounds() {
        return getBoxVolume() <= getMaxBoxVolume();
    }

    public boolean isBoxSideLengthInBounds() {
        return getBoxSideLength() <= getMaxBoxSideLength();
    }

    public boolean hasPermission() {
        return switch (state()) {
            case IDLE -> true;
            case PLACE_BLOCK -> limitationParams().generalConfig().allowPlaceBlocks();
            case BREAK_BLOCK -> limitationParams().generalConfig().allowBreakBlocks();
        };
    }




}
