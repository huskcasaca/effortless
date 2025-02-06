package xaero.pac.common.claims.player.api;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * API for dimension claims of a player
 */
public interface IPlayerDimensionClaimsAPI {

	/**
	 * Gets a stream of all claim position lists in this dimension for this player.
	 *
	 * @return the stream of all claim position lists, not null
	 */
	@Nonnull
	public Stream<IPlayerClaimPosListAPI> getStream();

}
