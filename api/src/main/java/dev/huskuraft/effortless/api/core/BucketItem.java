package dev.huskuraft.effortless.api.core;

import java.util.Objects;

import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.Fluids;

public interface BucketItem extends Item {

    static ItemStack createFilledResult(Player player, ItemStack emptyItemStack, ItemStack filledItemStack, boolean preventDuplicates) {
        var isCreative = player.getGameMode().isCreative();
        if (!isCreative) {
            emptyItemStack.decrease(1);
        }
        if (isCreative && preventDuplicates) {
//            if (!player.getInventory().contains(filledItemStack)) {
//                player.getInventory().addItem(filledItemStack);
//            }
            return emptyItemStack;
        }

        if (emptyItemStack.isEmpty()) {
            return filledItemStack;
        } else {
            if (!player.getInventory().addItem(filledItemStack)) {
                player.drop(filledItemStack, false, false);
            }
            return emptyItemStack;
        }
    }

    static ItemStack createFilledResult(Player player, ItemStack emptyItemStack, ItemStack filledItemStack) {
        return createFilledResult(player, emptyItemStack, filledItemStack, true);
    }

    Fluid getContent();

    boolean useContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction);

    void useExtraContent(World world, Player player, BlockPosition blockPosition, ItemStack itemStack);

    default boolean isEmpty() {
        return getContent() == null || Objects.equals(getContent().reference(), Fluids.EMPTY.fluid().reference());
    }

    @Override
    default InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction) {
        var itemStack = player.getItemStack(blockInteraction.getHand());
        var blockState = player.getWorld().getBlockState(blockInteraction.getBlockPosition());

        if (itemStack.getItem() instanceof BucketItem bucketItem) {
            if (bucketItem.isEmpty()) {
                var bucketCollectable = blockState.getBlock().getBucketCollectable();
                if (bucketCollectable != null) {
                    var collected = bucketCollectable.pickupBlock(player.getWorld(), player, blockInteraction.getBlockPosition(), blockState);
                    if (!collected.isEmpty()) {
                        var result = createFilledResult(player, itemStack, collected);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (blockState.getBlock().getLiquidPlaceable() != null || blockState.isAir() || blockState.canBeReplaced(bucketItem.getContent())) {
                    if (bucketItem.useContent(player.getWorld(), player, blockInteraction.getBlockPosition(), blockInteraction)) {
                        bucketItem.useExtraContent(player.getWorld(), player, blockInteraction.getBlockPosition(), itemStack);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

}
