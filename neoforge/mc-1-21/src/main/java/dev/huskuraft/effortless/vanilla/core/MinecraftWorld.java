package dev.huskuraft.effortless.vanilla.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.DimensionType;
import dev.huskuraft.effortless.api.core.FluidState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceKey;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.core.WorldBorder;
import net.minecraft.world.level.Level;

public record MinecraftWorld(Level refs) implements World {

    public static World ofNullable(Level refs) {
        if (refs == null) return null;
        return new MinecraftWorld(refs);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftPlayer.ofNullable(refs.getPlayerByUUID(uuid));
    }

    @Override
    public BlockState getBlockState(BlockPosition blockPosition) {
        return MinecraftBlockState.ofNullable(refs.getBlockState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public FluidState getFluidState(BlockPosition blockPosition) {
        return MinecraftFluidState.ofNullable(refs.getFluidState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public BlockEntity getBlockEntity(BlockPosition blockPosition) {
        return MinecraftBlockEntity.ofNullable(refs.getBlockEntity(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public boolean setBlock(BlockPosition blockPosition, BlockState blockState, int flags, int recursionLeft) {
        return refs.setBlock(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), flags, recursionLeft);
    }

    @Override
    public boolean isClient() {
        return refs.isClientSide();
    }

    @Override
    public ResourceKey<World> getDimensionId() {
        return new MinecraftResourceKey<>(refs.dimension());
    }

    @Override
    public DimensionType getDimensionType() {
        return new MinecraftDimensionType(refs.dimensionType());
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new MinecraftWorldBorder(refs.getWorldBorder());
    }

    @Override
    public boolean removeBlock(BlockPosition blockPosition, boolean moving) {
        return refs.removeBlock(MinecraftConvertor.toPlatformBlockPosition(blockPosition), moving);
    }
}
