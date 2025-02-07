package dev.ftb.mods.ftblibrary.math;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ChunkDimPos {

    public ChunkDimPos(ResourceKey<Level> dim, int x, int z) {
    }

    public ChunkDimPos(ResourceKey<Level> dim, ChunkPos pos) {
        this(dim, pos.x, pos.z);
    }

    public ChunkDimPos(Level world, BlockPos pos) {
        this(world.dimension(), pos.getX() >> 4, pos.getZ() >> 4);
    }

    public ChunkPos getChunkPos() {
        throw new RuntimeException("stub!");
    }

    public ChunkDimPos offset(int ox, int oz) {
        throw new RuntimeException("stub!");
    }

}
