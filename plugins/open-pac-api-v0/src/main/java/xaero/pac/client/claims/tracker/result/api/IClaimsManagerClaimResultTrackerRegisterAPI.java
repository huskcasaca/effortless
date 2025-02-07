package xaero.pac.client.claims.tracker.result.api;

import javax.annotation.Nonnull;

/**
 * API for a claim result tracker that lets you register listeners
 */
public interface IClaimsManagerClaimResultTrackerRegisterAPI {

    /**
     * Registers a claim result listener.
     * <p>
     * You can create one by implementing {@link IClaimsManagerClaimResultListenerAPI}.
     * <p>
     * You are not required to but it is recommended to register listeners during
     * the OPACClientAddonRegister.EVENT on Fabric or OPACClientAddonRegisterEvent on Forge.
     *
     * @param listener the listener to register, not null
     */
    void register(@Nonnull IClaimsManagerClaimResultListenerAPI listener);

}
