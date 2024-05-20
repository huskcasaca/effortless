package dev.huskuraft.effortless.api.core;


public interface BlockItem extends Item {

    Block getBlock();

    InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction);

    InteractionResult placeOnBlock(Player player, BlockInteraction blockInteraction);

    default boolean setBlockOnly(World world, Player player, BlockInteraction blockInteraction, BlockState blockState) {
        return world.setBlockAndUpdate(blockInteraction.getBlockPosition(), blockState);
    }

    boolean setBlockInWorld(World world, Player player, BlockInteraction blockInteraction, BlockState blockState);

    boolean updateBlockEntityTag(World world, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack);

    BlockState updateBlockStateFromTag(World world, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack);
}
