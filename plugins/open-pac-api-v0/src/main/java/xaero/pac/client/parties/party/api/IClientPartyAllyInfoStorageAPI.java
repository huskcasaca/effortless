package xaero.pac.client.parties.party.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for party ally info storage
 */
public interface IClientPartyAllyInfoStorageAPI {

    /**
     * Gets info about an ally party with the specified ID.
     *
     * @param id the UUID of the ally party, not null
     * @return the info about the ally party, null if it doesn't exist
     */
    @Nullable
    IClientPartyAllyInfoAPI get(@Nonnull UUID id);

}
