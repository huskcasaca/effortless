package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Single extends AbstractBlockStructure {

    protected static BlockInteraction traceSingle(Player player, Context context) {
        return traceSingle(player, context.buildState(), context.replaceMode(), context.maxReachDistance());
    }


    protected static BlockInteraction traceSingle(Player player, BuildState buildState, ReplaceMode replaceMode, int maxReachDistance) {
        var isHoldingEmptyBucket = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && bucketItem.isEmpty();
        var isHoldingNonBlockItem = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && !bucketItem.isEmpty();

        var interaction = player.raytrace(context.maxReachDistance(), 0, isHoldingEmptyBucket);

        var startBlockPosition = interaction.getBlockPosition();

        var isTracingTarget = context.state() == BuildState.BREAK_BLOCK || context.state() == BuildState.INTERACT_BLOCK;
        var isReplaceBlock = context.replaceMode().isQuick() || player.getWorld().getBlockState(startBlockPosition).isReplaceable(player, interaction);

        if ((isHoldingNonBlockItem && context.state() == BuildState.INTERACT_BLOCK || !isTracingTarget) && !isReplaceBlock) {
            startBlockPosition = startBlockPosition.relative(interaction.getDirection());
        }

        return interaction.withBlockPosition(startBlockPosition);
    }

    public static Stream<BlockPosition> collectSingleBlocks(Context context) {
        return Stream.of(context.firstBlockInteraction().getBlockPosition());
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return traceSingle(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return collectSingleBlocks(context);
    }

    @Override
    public int totalClicks(Context context) {
        return 1;
    }

}
