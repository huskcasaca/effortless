package dev.huskuraft.effortless.vanilla.plugin.openpac;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.ChunkPosition;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaim;
import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaimsManager;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import xaero.pac.common.claims.api.IClaimsManagerAPI;

public record MinecraftChunkClaimsManager(IClaimsManagerAPI refs) implements ChunkClaimsManager {

    public static ChunkClaimsManager ofNullable(IClaimsManagerAPI refs) {
        if (refs == null) return null;
        return new MinecraftChunkClaimsManager(refs);
    }

    @Override
    public @Nullable ChunkClaim get(@Nonnull ResourceLocation dimension, int x, int z) {
        var chunkClaim = refs.get(dimension.reference(), x, z);
        return MinecraftChunkClaim.ofNullable(chunkClaim);
    }

    @Override
    public @Nullable ChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull ChunkPosition chunkPosition) {
        var chunkPos = MinecraftConvertor.toPlatformChunkPosition(chunkPosition);
        var chunkClaim = refs.get(dimension.reference(), chunkPos);
        return MinecraftChunkClaim.ofNullable(chunkClaim);
    }

    @Override
    public @Nullable ChunkClaim get(@Nonnull ResourceLocation dimension, @Nonnull BlockPosition blockPosition) {
        var blockPos = MinecraftConvertor.toPlatformBlockPosition(blockPosition);
        var chunkClaim = refs.get(dimension.reference(), blockPos);
        return MinecraftChunkClaim.ofNullable(chunkClaim);
    }

}
