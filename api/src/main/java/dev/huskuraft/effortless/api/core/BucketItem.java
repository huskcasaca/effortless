package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.Fluids;

public interface BucketItem extends Item {

    Fluid getContent();

    boolean useContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction);

    void useExtraContent(World world, Player player, BlockPosition blockPosition, ItemStack itemStack);

    default boolean isEmpty() {
        return getContent().referenceValue().equals(Fluids.EMPTY.referenceValue());
    }

    @Override
    default InteractionResult use(Player player, BlockInteraction blockInteraction) {
        var itemStack = player.getItemStack(blockInteraction.getHand());
        var blockState = player.getWorld().getBlockState(blockInteraction.getBlockPosition());

        if (itemStack.getItem() instanceof BucketItem bucketItem) {
            if (bucketItem.isEmpty()) {
                var bucketCollectable = blockState.getBlock().getBucketCollectable();
                if (bucketCollectable != null) {
                    var collected = bucketCollectable.pickupBlock(player.getWorld(), player, blockInteraction.getBlockPosition(), blockState);
                    if (!collected.isEmpty()) {
                        player.awardStat(StatTypes.ITEM_USED.get(bucketItem));
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (blockState.getBlock().getLiquidPlaceable() != null || blockState.isAir() || blockState.canReplace(bucketItem.getContent())) {
                    if (bucketItem.useContent(player.getWorld(), player, blockInteraction.getBlockPosition(), blockInteraction)) {
                        bucketItem.useExtraContent(player.getWorld(), player, blockInteraction.getBlockPosition(), itemStack);
                        player.awardStat(StatTypes.ITEM_USED.get(bucketItem));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
