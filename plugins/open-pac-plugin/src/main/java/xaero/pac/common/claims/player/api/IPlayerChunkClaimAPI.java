package xaero.pac.common.claims.player.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for a chunk claim state
 */
public interface IPlayerChunkClaimAPI {

    /**
     * Checks whether this claim is marked for forceloading.
     * <p>
     * It doesn't mean that it's actually currently forceloaded.
     *
     * @return true if the claim is marked for forceloading, otherwise false
     */
    boolean isForceloadable();

    /**
     * Gets the UUID of the owner of this claim.
     *
     * @return the UUID of this claim's owner, not null
     */
    @Nonnull
    UUID getPlayerId();

    /**
     * Gets the sub-config index of this claim.
     *
     * @return the index corresponding to the sub-config used by this claim
     */
    int getSubConfigIndex();

    /**
     * Checks if another claim state is of the same type as this, which ignores
     * whether the claim states are forceloadable.
     *
     * @param other the other claim state, can be null
     * @return true if the specified claim state is of the same type as this,
     * otherwise false
     */
    boolean isSameClaimType(@Nullable IPlayerChunkClaimAPI other);

}
