package xaero.pac.client.claims.tracker.result.api;

import javax.annotation.Nonnull;

import xaero.pac.common.claims.result.api.AreaClaimResult;

/**
 * The interface to be implemented by all claim result listeners
 * <p>
 * Register your listeners in {@link IClaimsManagerClaimResultTrackerAPI}.
 */
public interface IClaimsManagerClaimResultListenerAPI {

    /**
     * Called when the client receives a claim result from the server.
     * <p>
     * Override this method and register the listener in {@link IClaimsManagerClaimResultTrackerAPI} to handle claim results
     * however you'd like.
     *
     * @param result the area claim result, not null
     */
    void onClaimResult(@Nonnull AreaClaimResult result);

}
