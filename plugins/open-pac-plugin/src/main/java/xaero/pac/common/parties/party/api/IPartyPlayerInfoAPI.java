package xaero.pac.common.parties.party.api;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * API for info about a player used for party purposes
 */
public interface IPartyPlayerInfoAPI {

    /**
     * Gets the UUID of this player.
     *
     * @return the UUID of this player, not null
     */
    @Nonnull
    UUID getUUID();

    /**
     * Gets the currently known username of this player.
     *
     * @return the currently known username of this player, not null
     */
    @Nonnull
    String getUsername();

}
