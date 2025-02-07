package dev.huskuraft.effortless.vanilla.plugin.ftbchunks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.plugin.ftbchunks.FtbClaimedChunk;
import dev.huskuraft.effortless.api.plugin.ftbchunks.FtbChunkClaimsManager;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;

public record FtbChunkClaimsManagerImpl(ClaimedChunkManager refs) implements FtbChunkClaimsManager {

    public static FtbChunkClaimsManager ofNullable(ClaimedChunkManager refs) {
        if (refs == null) return null;
        return new FtbChunkClaimsManagerImpl(refs);
    }

    @Override
    public @Nullable FtbClaimedChunk get(@Nonnull World world, @Nonnull BlockPosition blockPosition) {
        var chunk = refs.getChunk(new ChunkDimPos(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition)));
        return FtbClaimedChunkImpl.ofNullable(chunk);
    }


}
