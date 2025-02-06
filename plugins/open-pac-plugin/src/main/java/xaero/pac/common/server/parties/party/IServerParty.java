package xaero.pac.common.server.parties.party;

import xaero.pac.common.parties.party.IParty;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.ally.api.IPartyAllyAPI;
import xaero.pac.common.parties.party.api.IPartyPlayerInfoAPI;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.parties.party.member.PartyMemberRank;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;
import xaero.pac.common.server.expiration.ObjectManagerIOExpirableObject;
import xaero.pac.common.server.info.ServerInfo;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface IServerParty
<
	M extends IPartyMember,
	I extends IPartyPlayerInfo,
	A extends IPartyAlly
> extends IServerPartyAPI, IParty<M, I, A>, ObjectManagerIOExpirableObject {

	//internal api

	public boolean updateUsername(M member, String username);

	public A getAlly(String ownerUsername);

	public I getInvite(String username);

	@Override
	@Nullable
	public M getMemberInfo(@Nonnull String username);

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyMemberAPI> getMemberInfoStream() {
		return (Stream<IPartyMemberAPI>)(Object)getTypedMemberInfoStream();
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyMemberAPI> getStaffInfoStream() {
		return (Stream<IPartyMemberAPI>)(Object)getTypedStaffInfoStream();
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyMemberAPI> getNonStaffInfoStream() {
		return (Stream<IPartyMemberAPI>)(Object)getTypedNonStaffInfoStream();
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyPlayerInfoAPI> getInvitedPlayersStream() {
		return (Stream<IPartyPlayerInfoAPI>)(Object)getTypedInvitedPlayersStream();
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyAllyAPI> getAllyPartiesStream() {
		return (Stream<IPartyAllyAPI>)(Object)getTypedAllyPartiesStream();
	}

	@Override
	@SuppressWarnings("unchecked")
	default boolean setRank(@Nonnull IPartyMemberAPI member, @Nonnull PartyMemberRank rank) {
		return setRankTyped((M)member, rank);
	}

}
