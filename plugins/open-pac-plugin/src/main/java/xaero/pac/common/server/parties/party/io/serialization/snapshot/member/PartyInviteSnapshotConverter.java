package xaero.pac.common.server.parties.party.io.serialization.snapshot.member;

import xaero.pac.common.parties.party.member.PartyInvite;

import java.util.UUID;

public class PartyInviteSnapshotConverter {

	public PartyInvite convert(PartyInviteSnapshot data) {
		PartyInvite result = create(UUID.fromString(data.getUUID()));
		result.setUsername(data.getUsername());
		return result;
	}

	public PartyInviteSnapshot convert(PartyInvite partyPlayerInfo) {
		PartyInviteSnapshot result = new PartyInviteSnapshot(partyPlayerInfo.getUUID().toString(), partyPlayerInfo.getUsername());
		return result;
	}

	protected PartyInvite create(UUID playerId) {
		return new PartyInvite(playerId);
	}

}
