package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface BlockEntity extends PlatformReference {

    BlockState getBlockState();

    BlockPosition getBlockPosition();

    World getWorld();

}
