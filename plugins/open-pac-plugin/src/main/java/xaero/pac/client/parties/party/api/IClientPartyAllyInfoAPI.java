package xaero.pac.client.parties.party.api;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * API for info about an ally party
 */
public interface IClientPartyAllyInfoAPI {

    /**
     * Gets the UUID of the ally party.
     *
     * @return the UUID of the ally party, not null
     */
    @Nonnull
    UUID getAllyId();

    /**
     * Gets the configured custom name of the ally party.
     *
     * @return the custom name of the ally party, not null
     */
    @Nonnull
    String getAllyName();

    /**
     * Gets the default name of the ally party.
     *
     * @return the default name of the ally party, not null
     */
    @Nonnull
    String getAllyDefaultName();

}
