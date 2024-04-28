package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.Fluids;

public interface BucketItem extends Item {

    Fluid getContent();

    boolean emptyContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction);

    default boolean isEmpty() {
        return getContent().referenceValue().equals(Fluids.EMPTY.referenceValue());
    }

}
