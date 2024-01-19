package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MinecraftWorld implements World {

    private final Level reference;

    public MinecraftWorld(Level reference) {
        this.reference = reference;
    }

    @Override
    public Level referenceValue() {
        return reference;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return new MinecraftPlayer(reference.getPlayerByUUID(uuid));
    }

    @Override
    public BlockState getBlockState(BlockPosition blockPosition) {
        return new MinecraftBlockState(reference.getBlockState(MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
    }

    @Override
    public boolean setBlockState(BlockPosition blockPosition, BlockState blockState) {
        return reference.setBlockAndUpdate(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference());
    }

    @Override
    public boolean isClient() {
        return reference.isClientSide();
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftWorld world && reference.equals(world.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
