package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface World extends PlatformReference {

    Player getPlayer(UUID uuid);

    BlockState getBlockState(BlockPosition blockPosition);

    FluidState getFluidState(BlockPosition blockPosition);

    BlockEntity getBlockEntity(BlockPosition blockPosition);

    default BlockEntity getBlockEntityCopied(BlockPosition blockPosition) {
        if (getBlockEntity(blockPosition) == null) {
            return null;
        }
        return getBlockEntity(blockPosition).copy();
    }

    boolean setBlock(BlockPosition blockPosition, BlockState blockState, int flags, int recursionLeft);

    default boolean setBlock(BlockPosition blockPosition, BlockState blockState, int flags) {
        return setBlock(blockPosition, blockState, flags, 512);
    }

    default boolean setBlockAndUpdate(BlockPosition blockPosition, BlockState blockState) {
        return setBlock(blockPosition, blockState, 3);
    }

    default boolean removeBlock(BlockPosition blockPosition, boolean moving) {
        return setBlock(blockPosition, getFluidState(blockPosition).createLegacyBlock(), 3 | (moving ? 64 : 0));
    }

    boolean isClient();

    ResourceKey<World> getDimensionId();

    DimensionType getDimensionType();

    default int getMinBuildHeight() {
        return getDimensionType().minY();
    }

    default int getMaxBuildHeight() {
        return getDimensionType().minY() + getDimensionType().height();
    }

    WorldBorder getWorldBorder();

}
