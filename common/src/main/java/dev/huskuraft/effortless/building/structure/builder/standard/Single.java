package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BlockBuildStructure;

public record Single() implements BlockBuildStructure {

    protected static BlockInteraction traceSingle(Player player, Context context) {
        return traceSingle(player, context.buildState(), context.replaceMode(), context.maxReachDistance());
    }

    protected static BlockInteraction traceSingle(Player player, Context context, int maxReachDistance) {
        return traceSingle(player, context.buildState(), context.replaceMode(), maxReachDistance);
    }

    protected static BlockInteraction traceSingle(Player player, BuildState buildState, ReplaceMode replaceMode, int maxReachDistance) {
        var isHoldingEmptyBucket = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && bucketItem.isEmpty();
        var isHoldingNonBlockItem = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && !bucketItem.isEmpty();

        var interaction = player.raytrace(maxReachDistance, 0, isHoldingEmptyBucket);

        var startBlockPosition = interaction.getBlockPosition();

        var isTracingTarget = buildState == BuildState.BREAK_BLOCK || buildState == BuildState.INTERACT_BLOCK;
        var isReplaceBlock = replaceMode.isQuick() || player.getWorld().getBlockState(startBlockPosition).canBeReplaced(player, interaction);

        if ((isHoldingNonBlockItem && buildState == BuildState.INTERACT_BLOCK || !isTracingTarget) && !isReplaceBlock) {
            startBlockPosition = startBlockPosition.relative(interaction.getDirection());
        }

        return interaction.withBlockPosition(startBlockPosition);
    }

    public static Stream<BlockPosition> collectSingleBlocks(Context context) {
        return Stream.of(context.getPosition(0));
    }

    public static void addSingleBlock(Set<BlockPosition> set, BlockPosition pos1) {
        set.add(pos1);
    }

    public static void addSingleBlock(Set<BlockPosition> set, int x1, int y1, int z1) {
        set.add(new BlockPosition(x1, y1, z1));
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 1;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.SINGLE;
    }
}
