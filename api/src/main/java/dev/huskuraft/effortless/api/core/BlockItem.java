package dev.huskuraft.effortless.api.core;

public interface BlockItem extends Item {

    Block getBlock();

    InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction);

    InteractionResult placeOnBlock(Player player, BlockInteraction blockInteraction);

    default boolean setBlockOnly(World world, Player player, BlockInteraction blockInteraction, BlockState blockState) {
        return world.setBlockAndUpdate(blockInteraction.getBlockPosition(), blockState);
    }

    // FIXME: 25/6/24
    default boolean updateBlockEntityTag(World world, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return true;
    }


}
