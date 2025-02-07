package dev.ftb.mods.ftbteams.api.client;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Used to track known teams and player on each client in the game. You can retrieve an instance of this via
 * {@link FTBTeamsAPI.API#getClientManager()}.
 */
public interface ClientTeamManager {
    /**
     * Get the unique ID for this team manager
     * @return a unique UUID
     */
    UUID getManagerId();

    /**
     * Is this manager valid? An existing client team manager is invalidated whenever a full sync is received from the
     * server; if you cache this object, you must check for validity each time you use it.
     *
     * @return true if the manager is valid for use
     */
    boolean isValid();

    /**
     * Get an unmodifiable collection of the known players available at this time, as sync'd from the server.
     *
     * @return the known players
     */
    Collection<KnownClientPlayer> knownClientPlayers();

    /**
     * Get an unmodifiable collection of all teams known at this time, as sync'd from the server.
     *
     * @return the known teams
     */
    Collection<Team> getTeams();

    /**
     * Retrieve the given team by its unique ID. This is the name as returned by {@link Team#getId()}.
     *
     * @param teamId unique team ID
     * @return the team, or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getTeamByID(UUID teamId);

    /**
     * Get the client team data for this client player (i.e. {@link net.minecraft.client.Minecraft#player})
     *
     * @return this player's own team
     */
    Team selfTeam();

    /**
     * Get the known player data for this client player (i.e. {@link net.minecraft.client.Minecraft#player})
     *
     * @return this player's known data
     */
    KnownClientPlayer self();

    /**
     * Get the known player for the given UUID
     * @param id a unique player UUID
     * @return an optional, containing the player's known data or {@code Optional.empty()} if the given UUID is not known
     * to the client at this time
     */
    Optional<KnownClientPlayer> getKnownPlayer(UUID id);

    /**
     * Get a formatted name for the given player ID, for display purposes. This is used in the team chat window, for
     * example.
     *
     * @param id a player's unique id, may be null or {@link net.minecraft.Util#NIL_UUID} to indicate a "system" user.
     * @return a formatted and colored name for the given id, or {@code "Unknown"} if the ID is not known to the client at this time
     */
    Component formatName(@Nullable UUID id);
}
