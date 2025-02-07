package dev.ftb.mods.ftbteams.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface Team {
    /**
     * Get the unique ID for this team. For player teams, this is a valid player UUID. For party or server teams,
     * this is a random UUID assigned when the team was created.
     *
     * @return the unique team ID
     */
    UUID getId();

    /**
     * Get the unique effective ID for this team. For party and server teams, this is always the same as the result
     * of {#getId()}. For player teams, this is the same as {@code getId()} if the player is not in a party,
     * but is the ID of the party team if the player is in a party.
     *
     * @return the team ID for this team
     */
    UUID getTeamId();

    /**
     * Get the shortened team name, which is the team's display name, as returned by
     * <p>
     * {@code team.getProperty(TeamProperties.DISPLAY_NAME)}
     * </p>
     * followed by a '#', then by the first 8 characters of the team's full UUID, as returned by {#getId()}.
     * <p>
     * For example, {@code Herobrine#1A2B3C4D}, or {@code Notch_Party#5E6F8A9B}.
     * </p>
     *
     * @return the short team name
     */
    String getShortName();

    /**
     * Get the unique player ID for the team's owner.
     *
     * @return the owner's ID; a valid player UUID
     */
    UUID getOwner();

    /**
     * Retrieve a collection of all this team's known properties.
     *
     * @return the team's property collection
     */
//    TeamPropertyCollection getProperties();

    /**
     * Get the value for the given team property. See {TeamProperties} for the
     * built-in properties defined by FTB Teams. Other mods may register extra properties.
     *
     * @param property the property to retrieve
     * @return the retrieved value
     * @param <T> the property type
     */
//    <T> T getProperty(TeamProperty<T> property);

    /**
     * Set a new value for the given team property.
     *
     * @param property the property to update
     * @param value the new value
     * @param <T> the property type
     */
//    <T> void setProperty(TeamProperty<T> property, T value);

    /**
     * Check if this team is a player team, i.e. a player's personal team.
     *
     * @return true if the team is a player team, false otherwise
     */
    default boolean isPlayerTeam() {
        return false;
    }

    /**
     * Check if this team is a party team.
     *
     * @return true if the team is a party team, false otherwise
     */
    default boolean isPartyTeam() {
        return false;
    }

    /**
     * Check if this team is a server team.
     *
     * @return true if the team is a server team, false otherwise
     */
    default boolean isServerTeam() {
        return false;
    }

    /**
     * Get a non-modifiable view of the team's message history. Note that the history may be truncated, depending on
     * FTB Teams mod configuration settings.
     *
     * @return the team's message history
     */
//    List<TeamMessage> getMessageHistory();

    /**
     * Get the team rank for the given player ID. If the player is not currently in a party team, this will return
     * {@code TeamRank.OWNER}, i.e. a player is always considered the owner of their own personal team.
     *
     * @param playerId a valid player UUID
     * @return the player's rank in their current team
     */
//    TeamRank getRankForPlayer(UUID playerId);

    /**
     * Get a nicely marked-up name of this team, for display purposes. The name is colored according to the team
     * type (gray for player teams, aqua for party teams, red for server teams), and the returned text component
     * has a click action which runs {@code /ftbteams info ...} on the team.
     *
     * @return the formatted team name
     */
    Component getName();

    /**
     * Send a team message to this team. This is a no-op if called on a client-side team.
     *
     * @param senderId UUID of the player sending the message
     * @param message  the message text
     */
    void sendMessage(UUID senderId, String message);

    /**
     * Return a nicely marked-up block of text info for this team, for display purposes.
     *
     * @return a component list of team information
     */
    List<Component> getTeamInfo();

    /**
     * Helper method: return a map of member ID's to their rank for this team. The returned map should be treated as
     * immutable.
     *
     * @param minRank the minimum rank to include (pass {@code TeamRank.NONE} to include everyone)
     * @return a map of member ID's to their rank
     */
//    Map<UUID,TeamRank> getPlayersByRank(TeamRank minRank);

    /**
     * Get a set of the UUID's for all team members.
     *
     * @return UUID's of all team members
     */
    Set<UUID> getMembers();

    /**
     * Get a translation key for this team's type (i.e. player, party or server), for display purposes.
     *
     * @return a translation key
     */
    String getTypeTranslationKey();

    /**
     * Is this team a client-side team object? In most cases it should be obvious (if you're on the client, any
     * retrieved team object is a client-side team), but this method is provided as a contingency.
     *
     * @return true if this is a client-side team, false if server-side
     */
    default boolean isClientTeam() {
        return false;
    }

    /**
     * Get any extension data that may exist in this team manager. This is empty by default, but other mods can use
     * this to store mod-specific data where necessary.
     * <p>
     * This data is serialized along with the rest of the team data so persists across server restarts, but if you change
     * any data in the compound tag returned by this method, you should call {#markDirty()} to ensure your changes
     * actually get saved.
     *
     * @return extension data for the team
     */
    CompoundTag getExtraData();

    /**
     * Mark the team as requiring serialization. The only time this should be necessary to call is if you change
     * any data in the compound returned by {#getExtraData()}.
     */
    void markDirty();

    /**
     * Convenience method to retrieve a collection of all currently-online members of the team. This is not useful
     * client-side; it will always return an empty collection.
     *
     * @return all the server players who are currently online
     */
    Collection<ServerPlayer> getOnlineMembers();

    /**
     * Retrieve a colored text component for the team's name (which is also clickable, and will display basic team
     * info when clicked)
     *
     * @return a colored clickable text component
     */
    Component getColoredName();

    /**
     * Is this team still valid, i.e. hasn't been deleted (or on the client, overwritten by a sync from the server) ?
     * If you cache a Team object for any reason, you should always use this to check team validity.
     *
     * @return true if the team object is still valid, false otherwise
     */
    boolean isValid();

    /**
     * Create a party team from this team, which must be an individual player's team. The player must not be in an
     * existing team.
     *
     * @param description the long description text (an empty string is acceptable)
     * @param color the color for the new team; if null, then a random color is chosen
     * @return the newly-created party team
     */
//    Team createParty(String description, @Nullable Color4I color);
}
