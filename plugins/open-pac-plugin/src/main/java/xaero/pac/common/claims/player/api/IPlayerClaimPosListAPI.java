package xaero.pac.common.claims.player.api;

import net.minecraft.world.level.ChunkPos;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

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
	public IPlayerChunkClaimAPI getClaimState();

	/**
	 * Gets a stream of all chunk positions in this list.
	 *
	 * @return the stream of all {@link ChunkPos} in this list, not null
	 */
	@Nonnull
	public Stream<ChunkPos> getStream();

	/**
	 * Gets the number of chunk positions in this list.
	 *
	 * @return the chunk position count
	 */
	public int getCount();

}
