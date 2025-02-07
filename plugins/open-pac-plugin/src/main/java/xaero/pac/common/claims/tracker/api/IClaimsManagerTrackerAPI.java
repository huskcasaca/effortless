package xaero.pac.common.claims.tracker.api;

import javax.annotation.Nonnull;

/**
 * API for a claims manager tracker.
 */
public interface IClaimsManagerTrackerAPI extends IClaimsManagerTrackerRegisterAPI {

    @Override
    void register(@Nonnull IClaimsManagerListenerAPI listener);

}
