package xaero.pac.common.server.parties.system.api;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The interface to be overridden by addons that wish to implement additional party systems to be used
 * by Open Parties and Claims (just the claiming feature as of writing this).
 * <p>
 * Player position synchronization is a part of the default party system. Similar functionality should
 * be implemented in other party systems and supported separately by the map mods that display party
 * members. OPAC does not synchronize or provide party member/ally positions from other party systems,
 * even if they're implemented using this interface.
 * <p>
 * Party system implementations must be registered in {@link IPlayerPartySystemRegisterAPI}.
 *
 * @param <P> the type of parties in the implemented system
 */
public interface IPlayerPartySystemAPI<P> {

    /**
     * Gets the party that the player with a specified UUID owns.
     *
     * @param playerId the UUID of the player, not null
     * @return the party that the player owns, null if the player doesn't own one
     */
    @Nullable
    P getPartyByOwner(@Nonnull UUID playerId);

    /**
     * Gets the party that the player with a specified UUID is a part of.
     *
     * @param playerId the UUID of the player, not null
     * @return the party that the player is in, null if the player isn't in one
     */
    @Nullable
    P getPartyByMember(@Nonnull UUID playerId);

    /**
     * Checks if a player with UUID {@code playerId} considers a player with UUID {@code potentialAllyPlayerId}
     * an ally.
     *
     * @param playerId              the UUID of the player to check the allies of, not null
     * @param potentialAllyPlayerId the UUID of the player to check the ally status of, not null
     * @return true, if {@code playerId} considers {@code potentialAllyPlayerId} an ally, otherwise false
     */
    boolean isPlayerAllying(@Nonnull UUID playerId, @Nonnull UUID potentialAllyPlayerId);

    /**
     * Checks if a player is permitted to claim as the party that they belong to but don't own.
     * <p>
     * This is not used by Open Parties and Claims as of writing this.
     *
     * @param playerId the UUID of the player, not null
     * @return true, if the player is permitted to claim as the party, otherwise false
     */
    boolean isPermittedToPartyClaim(@Nonnull UUID playerId);

}
