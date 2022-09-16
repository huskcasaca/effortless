package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.platform.Server;

import java.util.UUID;

public abstract class World {

    public abstract Player getPlayer(UUID uuid);

    public abstract Server getServer();

    public abstract BlockData getBlockData(BlockPosition blockPosition);

    public abstract boolean isClient();

    public abstract boolean isEnabledInAdventure(ItemStack itemStack, BlockPosition blockPosition);

    public abstract boolean isBlockPlaceable(BlockPosition blockPosition);

    public abstract boolean isBlockBreakable(BlockPosition blockPosition);

    public abstract boolean placeBlock(
            Player player,
            BlockInteraction interaction,
            BlockData blockData,
            ItemStack itemStack);

    public abstract boolean breakBlock(
            Player player,
            BlockInteraction interaction);

}
