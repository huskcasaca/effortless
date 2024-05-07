package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Single extends AbstractBlockStructure {

    protected static BlockInteraction traceSingle(Player player, Context context) {
        return traceSingle(player, context.buildState(), context.replaceMode(), context.maxReachDistance());
    }


    protected static BlockInteraction traceSingle(Player player, BuildState buildState, ReplaceMode replaceMode, int maxReachDistance) {
        var isHoldingEmptyBucket = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && bucketItem.isEmpty();
        var isHoldingNonBlockItem = player.getItemStack(InteractionHand.MAIN).getItem() instanceof BucketItem bucketItem && !bucketItem.isEmpty();

        var interaction = player.raytrace(maxReachDistance, 0, isHoldingEmptyBucket);

        var startBlockPosition = interaction.getBlockPosition();

        var isTracingTarget = buildState == BuildState.BREAK_BLOCK || buildState == BuildState.INTERACT_BLOCK;
        var isReplaceBlock = replaceMode.isQuick() || player.getWorld().getBlockState(startBlockPosition).isReplaceable(player, interaction);

        if ((isHoldingNonBlockItem && buildState == BuildState.INTERACT_BLOCK || !isTracingTarget) && !isReplaceBlock) {
            startBlockPosition = startBlockPosition.relative(interaction.getDirection());
        }

        return interaction.withBlockPosition(startBlockPosition);
    }

    public static Stream<BlockPosition> collectSingleBlocks(Context context) {
        return Stream.of(context.getPosition(0));
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
    public int totalInteractions(Context context) {
        return 1;
    }

}
