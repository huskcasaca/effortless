package xaero.pac.client.claims.tracker.result.api;

import javax.annotation.Nonnull;

/**
 * API for a claim result tracker.
 */
public interface IClaimsManagerClaimResultTrackerAPI extends IClaimsManagerClaimResultTrackerRegisterAPI {

    @Override
    void register(@Nonnull IClaimsManagerClaimResultListenerAPI listener);

}
