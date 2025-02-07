package dev.ftb.mods.ftblibrary.math;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public record ChunkDimPos(ResourceKey<Level> dimension, ChunkPos chunkPos) implements Comparable<ChunkDimPos> {

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
