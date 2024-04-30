package dev.huskuraft.effortless.api.core;


public interface BlockItem extends Item {

    Block getBlock();

    InteractionResult placeOnBlock(Player player, BlockInteraction blockInteraction);
}
