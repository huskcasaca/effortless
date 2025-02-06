package xaero.pac.client.parties.party.api;

import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.PartyMemberRank;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * API for a party on the client side
 */
public interface IClientPartyAPI extends IPartyAPI {

	@Override
	public int getMemberCount();

	@Override
	@Nullable
	public IPartyMemberAPI getMemberInfo(@Nonnull UUID memberUUID);

	@Override
	public int getAllyCount();

	@Override
	public boolean isAlly(@Nonnull UUID partyId);

	@Override
	public int getInviteCount();

	@Override
	public boolean isInvited(@Nonnull UUID playerId);

	@Nonnull
	@Override
	public Stream<IPartyMemberAPI> getMemberInfoStream();

	@Nonnull
	@Override
	public Stream<IPartyMemberAPI> getStaffInfoStream();

	@Nonnull
	@Override
	public Stream<IPartyMemberAPI> getNonStaffInfoStream();

	@Nonnull
	@Override
	public Stream<IPartyPlayerInfoAPI> getInvitedPlayersStream();

	@Nonnull
	@Override
	public Stream<IPartyAllyAPI> getAllyPartiesStream();

	@Nonnull
	@Override
	public IPartyMemberAPI getOwner();

	@Nonnull
	@Override
	public UUID getId();

	@Nonnull
	@Override
	public String getDefaultName();

	@Override
	public boolean setRank(@Nonnull IPartyMemberAPI member, @Nonnull PartyMemberRank rank);

}
