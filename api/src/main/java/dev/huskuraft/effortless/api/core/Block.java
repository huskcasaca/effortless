package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.core.fluid.BucketCollectable;
import dev.huskuraft.effortless.api.core.fluid.LiquidPlaceable;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Block extends PlatformReference {

    BlockState getDefaultBlockState();

    BlockState getBlockState(Player player, BlockInteraction interaction);

    BucketCollectable getBucketCollectable();

    LiquidPlaceable getLiquidPlaceable();

}
