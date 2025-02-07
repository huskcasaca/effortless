package dev.ftb.mods.ftblibrary.math;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ChunkDimPos {
    public final ResourceKey<Level> dimension;
    public final int x;
    public final int z;
    private ChunkPos chunkPos;
    private int hash;

    public ChunkDimPos(ResourceKey<Level> dim, int _x, int _z) {
        dimension = dim;
        x = _x;
        z = _z;
    }

    public ChunkDimPos(ResourceKey<Level> dim, ChunkPos pos) {
        this(dim, pos.x, pos.z);
    }

    public ChunkDimPos(Level world, BlockPos pos) {
        this(world.dimension(), pos.getX() >> 4, pos.getZ() >> 4);
    }

    public ChunkPos getChunkPos() {
        if (chunkPos == null) {
            chunkPos = new ChunkPos(x, z);
        }

        return chunkPos;
    }

    public ChunkDimPos offset(int ox, int oz) {
        return new ChunkDimPos(dimension, x + ox, z + oz);
    }
}
