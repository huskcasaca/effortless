package xaero.pac.common.claims.player.api;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

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
    Stream<IPlayerClaimPosListAPI> getStream();

}
