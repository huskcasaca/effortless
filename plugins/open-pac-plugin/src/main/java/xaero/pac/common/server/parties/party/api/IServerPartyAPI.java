package xaero.pac.common.server.parties.party.api;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.PartyMemberRank;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

/**
 * API for a party on the server side
 */
public interface IServerPartyAPI extends IPartyAPI {

    @Override
    int getMemberCount();

    @Override
    @Nullable
    IPartyMemberAPI getMemberInfo(@Nonnull UUID memberUUID);

    @Override
    int getAllyCount();

    @Override
    boolean isAlly(@Nonnull UUID partyId);

    @Override
    int getInviteCount();

    @Override
    boolean isInvited(@Nonnull UUID playerId);

    @Nonnull
    @Override
    Stream<IPartyMemberAPI> getMemberInfoStream();

    @Nonnull
    @Override
    Stream<IPartyMemberAPI> getStaffInfoStream();

    @Nonnull
    @Override
    Stream<IPartyMemberAPI> getNonStaffInfoStream();

    @Nonnull
    @Override
    Stream<IPartyPlayerInfoAPI> getInvitedPlayersStream();

    @Nonnull
    @Override
    Stream<IPartyAllyAPI> getAllyPartiesStream();

    @Nonnull
    @Override
    IPartyMemberAPI getOwner();

    @Nonnull
    @Override
    UUID getId();

    @Nonnull
    @Override
    String getDefaultName();

    @Override
    boolean setRank(@Nonnull IPartyMemberAPI member, @Nonnull PartyMemberRank rank);

    /**
     * Adds a new party member with specified player UUID, rank and username.
     *
     * @param playerUUID     the UUID of a player, not null
     * @param rank           the rank for the party member, null for the default rank
     * @param playerUsername the current username of the player, not null
     * @return the created party member info, null if the player is already in a party
     */
    @Nullable
    IPartyMemberAPI addMember(@Nonnull UUID playerUUID, @Nullable PartyMemberRank rank, @Nonnull String playerUsername);

    /**
     * Removes the party member with a specified player UUID, unless the player is the owner of this party.
     *
     * @param playerUUID the UUID of a player, not null
     * @return the removed party member info, null if the specified player is the party owner
     * or if the player isn't in the party
     */
    @Nullable
    IPartyMemberAPI removeMember(@Nonnull UUID playerUUID);

    /**
     * Gets info about the party member with a specified username.
     *
     * @param username the username of a party member, not null
     * @return the member info, null if doesn't exist
     */
    @Nullable
    IPartyMemberAPI getMemberInfo(@Nonnull String username);

    /**
     * Adds a new ally party to this party.
     *
     * @param partyId the UUID of the party to ally, not null
     */
    void addAllyParty(@Nonnull UUID partyId);

    /**
     * Removes an ally party from this party.
     *
     * @param partyId the UUID of the party to unally, not null
     */
    void removeAllyParty(@Nonnull UUID partyId);

    /**
     * Invites the player with specified player UUID and username to this party.
     *
     * @param playerUUID     the UUID of the player, not null
     * @param playerUsername the current username of the player, not null
     * @return the invitation info, null if the player is already invited to this party
     */
    @Nullable
    IPartyPlayerInfoAPI invitePlayer(@Nonnull UUID playerUUID, @Nonnull String playerUsername);

    /**
     * Removes the player invitation for a specified player UUID.
     *
     * @param playerUUID the UUID of the player, not null
     * @return the removed invitation info, null if the player has not been invited
     */
    @Nullable
    IPartyPlayerInfoAPI uninvitePlayer(@Nonnull UUID playerUUID);

    /**
     * Gets a stream of all currently online members of this party.
     *
     * @return the stream of online party members, not null
     */
    @Nonnull
    Stream<ServerPlayer> getOnlineMemberStream();

}
