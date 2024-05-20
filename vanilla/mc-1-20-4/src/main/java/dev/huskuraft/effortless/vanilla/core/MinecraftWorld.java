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

public record MinecraftWorld(Level referenceValue) implements World {

    public static World ofNullable(Level reference) {
        return reference == null ? null : new MinecraftWorld(reference);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftPlayer.ofNullable(referenceValue().getPlayerByUUID(uuid));
    }

    @Override
    public BlockState getBlockState(BlockPosition blockPosition) {
        return MinecraftBlockState.ofNullable(referenceValue().getBlockState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public FluidState getFluidState(BlockPosition blockPosition) {
        return MinecraftFluidState.ofNullable(referenceValue().getFluidState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public BlockEntity getBlockEntity(BlockPosition blockPosition) {
        return MinecraftBlockEntity.ofNullable(referenceValue().getBlockEntity(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public boolean setBlock(BlockPosition blockPosition, BlockState blockState, int flags, int recursionLeft) {
        return referenceValue().setBlock(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), flags, recursionLeft);
    }

    @Override
    public boolean isClient() {
        return referenceValue().isClientSide();
    }

    @Override
    public ResourceKey<World> getDimensionId() {
        return new MinecraftResourceKey<>(referenceValue().dimension());
    }

    @Override
    public DimensionType getDimensionType() {
        return new MinecraftDimensionType(referenceValue().dimensionType());
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new MinecraftWorldBorder(referenceValue().getWorldBorder());
    }

    @Override
    public boolean removeBlock(BlockPosition blockPosition, boolean moving) {
        return referenceValue().removeBlock(MinecraftConvertor.toPlatformBlockPosition(blockPosition), moving);
    }
}
