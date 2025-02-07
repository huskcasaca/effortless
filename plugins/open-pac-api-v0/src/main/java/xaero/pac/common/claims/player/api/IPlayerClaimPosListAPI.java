package xaero.pac.common.claims.player.api;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.world.level.ChunkPos;

/**
 * API for a claim state position list
 */
public interface IPlayerClaimPosListAPI {

    /**
     * Gets the claim state that all chunk positions in this list have.
     *
     * @return the claim state for this list, not null
     */
    @Nonnull
    IPlayerChunkClaimAPI getClaimState();

    /**
     * Gets a stream of all chunk positions in this list.
     *
     * @return the stream of all {@link ChunkPos} in this list, not null
     */
    @Nonnull
    Stream<ChunkPos> getStream();

    /**
     * Gets the number of chunk positions in this list.
     *
     * @return the chunk position count
     */
    int getCount();

}
