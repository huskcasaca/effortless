package xaero.pac.common.claims.api;

import javax.annotation.Nullable;

import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

/**
 * API for region claims
 */
public interface IRegionClaimsAPI {

    /**
     * Gets the claim state at a specified chunk location inside the 512x512 region.
     * <p>
     * The coordinate values must be within 0 - 31 (inclusive).
     *
     * @param x the X coordinate of the chunk inside the region, 0 - 31 (inclusive)
     * @param z the Z coordinate of the chunk inside the region, 0 - 31 (inclusive)
     * @return the claim state, null if the chunk is not claimed
     */
    @Nullable
    IPlayerChunkClaimAPI get(int x, int z);

    /**
     * Gets the X coordinate of the 512x512 region.
     *
     * @return the X coordinate value
     */
    int getX();

    /**
     * Gets the Z coordinate of the 512x512 region.
     *
     * @return the Z coordinate value
     */
    int getZ();

}
