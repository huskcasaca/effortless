package dev.ftb.mods.ftbteams.api;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Top-level interface for querying team data on the server. Retrieve an instance of this via
 * {FTBTeamsAPI.API#getManager()}.
 */
public interface TeamManager {
    /**
     * Convenience method to get the current Minecraft server.
     *
     * @return the server
     */
    MinecraftServer getServer();

    /**
     * Get the unique ID for this team manager, which is a random UUID assigned on first creation.
     *
     * @return the unique manager ID
     */
    UUID getId();

    /**
     * Get an immutable collection of all teams known to the team manager.
     *
     * @return an immutable view of the known teams
     */
    Collection<Team> getTeams();

    /**
     * Get the team for the given player ID, if it exists.
     *
     * @param uuid a player UUID
     * @return the player's team (maybe a party team), or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getTeamForPlayerID(UUID uuid);

    /**
     * Get the team for the given player, if it exists.
     *
     * @param player a server-side player object
     * @return the player's team (maybe a party team), or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getTeamForPlayer(ServerPlayer player);

    /**
     * Get the player's own team, given their player ID. This always returns the player's personal team, even if
     * they are currently in a party team.
     *
     * @param uuid the player's UUID
     * @return the player's personal team, or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getPlayerTeamForPlayerID(UUID uuid);

    /**
     * Retrieve the given team by short (friendly) name. This is the name as returned by {Team#getShortName()}.
     *
     * @param teamName a team name
     * @return the team, or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getTeamByName(String teamName);

    /**
     * Retrieve the given team by its unique ID. This is the name as returned by {Team#getId()}.
     *
     * @param teamId unique team ID
     * @return the team, or {@code Optional.empty()} if no team could be found
     */
    Optional<Team> getTeamByID(UUID teamId);

    /**
     * Convenience method to check if two player IDs are in the same team.
     *
     * @param id1 UUID of the first player
     * @param id2 UUID of the first player
     * @return true if the players are in the same team
     */
    boolean arePlayersInSameTeam(UUID id1, UUID id2);

    /**
     * Get an unmodifiable map of player UUID to Team object for all known player teams. Note that this does not
     * include party teams.
     *
     * @return all known player teams
     */
    Map<UUID, ? extends Team> getKnownPlayerTeams();

    /**
     * Get any extension data that may exist in this team manager. This is empty by default, but other mods can use
     * this to store manager-specific data where necessary.
     * <p>
     * This data is serialized along with the rest of the manager so persists across server restarts, but if you change
     * any data in the compound tag returned by this method, you should call {#markDirty()} to ensure your changes
     * actually get saved.
     *
     * @return extension data for the manager
     */
    CompoundTag getExtraData();

    /**
     * Mark the manager as requiring serialization. The only time this should be necessary to call is if you change
     * any data in the compound returned by {#getExtraData()}.
     */
    void markDirty();

    /**
     * Attempt to create a party team for the given player
     * @param player the player to create the team for
     * @param name the human-readable team name (something like "{player}'s team" is suggested)
     * @param description a text description of the team, may be null
     * @param color a color definition; if null, a random color will be picked
     * @return the new party team
     * @throws CommandSyntaxException if there was any kind of failure creating the team,
     * most commonly that the player is already in a party
     */
//    Team createPartyTeam(ServerPlayer player, String name, @Nullable String description, @Nullable Color4I color) throws CommandSyntaxException;
}
