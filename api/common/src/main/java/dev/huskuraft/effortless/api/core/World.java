package dev.huskuraft.effortless.api.core;

import java.util.UUID;

public interface World {

    Player getPlayer(UUID uuid);

    BlockState getBlockState(BlockPosition blockPosition);

    boolean setBlockState(BlockPosition blockPosition, BlockState blockState);

    boolean isClient();

}
