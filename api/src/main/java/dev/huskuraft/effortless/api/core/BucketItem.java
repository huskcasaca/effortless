package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.core.fluid.Fluid;

public interface BucketItem extends Item {

    Fluid getContent();

    boolean emptyContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction);

}
