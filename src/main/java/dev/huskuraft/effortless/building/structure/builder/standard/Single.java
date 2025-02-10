package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BucketItem;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;

public record Single() implements BlockStructure {

    protected static BlockInteraction traceSingle(Player player, Context context) {
        return traceSingle(player, context.buildState(), context.replace().isQuick(), context.maxReachDistance());
    }

    protected static BlockInteraction traceSingle(Player player, Context context, int maxReachDistance) {
        return traceSingle(player, context.buildState(), context.replace().isQuick(), maxReachDistance);
    }

    protected static BlockInteraction traceSingle(Player player, BuildState buildState, boolean relative, int maxReachDistance) {
        var isHoldingEmptyBucket = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && bucketItem.isEmpty();
        var isHoldingNonBlockItem = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && !bucketItem.isEmpty();

        var interaction = player.raytrace(maxReachDistance, 0, isHoldingEmptyBucket);

        var startBlockPosition = interaction.getBlockPosition();

        var isTracingTarget = buildState == BuildState.BREAK_BLOCK || buildState == BuildState.INTERACT_BLOCK || buildState == BuildState.COPY_STRUCTURE;
        var isReplaceBlock = relative || player.getWorld().getBlockState(startBlockPosition).canBeReplaced(player, interaction);

        if ((isHoldingNonBlockItem && buildState == BuildState.INTERACT_BLOCK || !isTracingTarget) && !isReplaceBlock) {
            return interaction.withBlockPosition(startBlockPosition.relative(interaction.getDirection()));
        }

        return interaction;
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
