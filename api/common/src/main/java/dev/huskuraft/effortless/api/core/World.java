package dev.huskuraft.effortless.api.core;

import java.util.UUID;

public abstract class World {

    public abstract Player getPlayer(UUID uuid);

    public abstract BlockState getBlockState(BlockPosition blockPosition);

    public abstract boolean setBlockState(BlockPosition blockPosition, BlockState blockState);

    public abstract boolean isClient();

}
