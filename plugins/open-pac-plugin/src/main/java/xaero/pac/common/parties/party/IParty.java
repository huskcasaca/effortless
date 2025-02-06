package xaero.pac.common.parties.party;

import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.api.IPartyAPI;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.parties.party.member.PartyMemberRank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Stream;

public interface IParty<M extends IPartyMember, I extends IPartyPlayerInfo, A extends IPartyAlly> extends IPartyAPI {

	//internal api

	@Nullable
	public M getMemberInfo(@Nonnull UUID memberUUID);

	@Nonnull
	@Override
	M getOwner();

	public boolean changeOwner(UUID newOwnerId, String newOwnerUsername);

	public M addMember(UUID playerUUID, PartyMemberRank rank, String playerUsername);

	public M removeMember(UUID playerUUID);

	public void addAllyParty(UUID partyId);

	public void removeAllyParty(UUID partyId);

	public I invitePlayer(UUID playerUUID, String playerUsername);

	public I uninvitePlayer(UUID playerUUID);

	@Nonnull
	public Stream<M> getTypedMemberInfoStream();

	@Nonnull
	public Stream<M> getTypedStaffInfoStream();

	@Nonnull
	public Stream<M> getTypedNonStaffInfoStream();

	@Nonnull
	public Stream<I> getTypedInvitedPlayersStream();

	@Nonnull
	public Stream<A> getTypedAllyPartiesStream();

	public boolean setRankTyped(@Nonnull M member, @Nonnull PartyMemberRank rank);

}
