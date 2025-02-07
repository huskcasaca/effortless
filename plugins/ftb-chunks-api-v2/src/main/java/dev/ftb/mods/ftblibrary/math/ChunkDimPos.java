package dev.ftb.mods.ftblibrary.math;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public record ChunkDimPos(ResourceKey<Level> dimension, ChunkPos chunkPos) implements Comparable<ChunkDimPos> {

    public ChunkDimPos(ResourceKey<Level> dim, int x, int z) {
        this(dim, new ChunkPos(x, z));
    }

    public ChunkDimPos(Level world, BlockPos pos) {
        this(world.dimension(), pos.getX() >> 4, pos.getZ() >> 4);
    }

    public int x() {
        return chunkPos.x;
    }

    public int z() {
        return chunkPos.z;
    }

    public ResourceKey<Level> dimension() {
        return dimension;
    }

    @Override
    public int compareTo(ChunkDimPos o) {
        throw new RuntimeException("stub!");
    }

    public ChunkDimPos offset(int ox, int oz) {
        throw new RuntimeException("stub!");
    }
}
