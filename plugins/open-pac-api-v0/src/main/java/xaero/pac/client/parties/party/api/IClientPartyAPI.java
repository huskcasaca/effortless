package xaero.pac.client.parties.party.api;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.PartyMemberRank;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

/**
 * API for a party on the client side
 */
public interface IClientPartyAPI extends IPartyAPI {

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

}
