package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface World extends PlatformReference {

    Player getPlayer(UUID uuid);

    BlockState getBlockState(BlockPosition blockPosition);

    boolean setBlockState(BlockPosition blockPosition, BlockState blockState);

    boolean isClient();

}
