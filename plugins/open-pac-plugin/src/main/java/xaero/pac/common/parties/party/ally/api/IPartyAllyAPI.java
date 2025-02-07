package xaero.pac.common.parties.party.ally.api;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * An ally of a party
 */
public interface IPartyAllyAPI {

    /**
     * Gets the UUID of the ally party.
     *
     * @return the UUID of the ally, not null
     */
    @Nonnull
    UUID getPartyId();

}
