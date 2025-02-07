package dev.huskuraft.effortless.api.plugin.openpac;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.ChunkPosition;
import dev.huskuraft.effortless.api.core.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface OpenPacChunkClaimsManager {

    @Nullable
    OpenPacChunkClaim get(@Nonnull ResourceLocation dimension, int x, int z);

    @Nullable
    OpenPacChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull ChunkPosition chunkPosition);

    @Nullable
    OpenPacChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull BlockPosition blockPosition);
}
