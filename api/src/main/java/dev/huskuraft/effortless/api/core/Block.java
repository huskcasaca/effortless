package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.core.fluid.BucketCollectable;
import dev.huskuraft.effortless.api.core.fluid.LiquidPlaceable;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Block extends PlatformReference {

    BlockState getDefaultBlockState();

    BucketCollectable getBucketCollectable();

    LiquidPlaceable getLiquidPlaceable();

}
