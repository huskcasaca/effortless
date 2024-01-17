package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.util.UUID;

public interface World extends PlatformReference {

    Player getPlayer(UUID uuid);

    BlockState getBlockState(BlockPosition blockPosition);

    boolean setBlockState(BlockPosition blockPosition, BlockState blockState);

    boolean isClient();

}
