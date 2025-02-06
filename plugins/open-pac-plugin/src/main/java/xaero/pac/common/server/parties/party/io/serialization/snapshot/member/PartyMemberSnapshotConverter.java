package xaero.pac.common.server.parties.party.io.serialization.snapshot.member;

import xaero.pac.common.parties.party.PartyPlayerInfo;
import xaero.pac.common.parties.party.member.PartyMember;

import java.util.UUID;

public class PartyMemberSnapshotConverter {

	public PartyMember convert(PartyMemberSnapshot data, boolean isOwner) {
		PartyMember result = new PartyMember(UUID.fromString(data.getUUID()), isOwner);
		result.setUsername(data.getUsername());
		result.setRank(data.getRank());
		return result;
	}

	public PartyMemberSnapshot convert(PartyPlayerInfo<?> partyMember) {
		PartyMemberSnapshot result = new PartyMemberSnapshot(partyMember.getUUID().toString(), partyMember.getUsername(), ((PartyMember) partyMember).getRank());
		return result;
	}

}
