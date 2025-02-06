package xaero.pac.common.claims.tracker.api;

import javax.annotation.Nonnull;

/**
 * API for registering claims manager listeners.
 */
public interface IClaimsManagerTrackerRegisterAPI {

    /**
     * Registers a claim change listener.
     * <p>
     * You can create one by implementing {@link IClaimsManagerListenerAPI}.
     * <p>
     * You are not required to but it is recommended to register listeners, on the
     * client side, during the OPACClientAddonRegister.EVENT on Fabric or OPACClientAddonRegisterEvent on Forge,
     * and, on the server side, during the OPACServerAddonRegister.EVENT on Fabric or OPACServerAddonRegisterEvent on Forge.
     *
     * @param listener the listener to register, not null
     */
    void register(@Nonnull IClaimsManagerListenerAPI listener);

}
