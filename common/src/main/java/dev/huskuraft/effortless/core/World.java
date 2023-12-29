package dev.huskuraft.effortless.core;

import java.util.UUID;

public abstract class World {

    public abstract Player getPlayer(UUID uuid);

    public abstract BlockData getBlockData(BlockPosition blockPosition);

    public abstract boolean setBlockData(BlockPosition blockPosition, BlockData blockData);

    public abstract boolean isClient();

}
