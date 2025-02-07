package dev.huskuraft.effortless.api.plugin.ftbchunks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.World;

public interface FtbChunkClaimsManager {

    @Nullable
    FtbClaimedChunk get(@Nonnull World world, @Nonnull BlockPosition blockPosition);

}
