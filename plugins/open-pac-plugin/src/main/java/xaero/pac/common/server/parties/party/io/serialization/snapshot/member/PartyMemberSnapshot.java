package xaero.pac.common.server.parties.party.io.serialization.snapshot.member;

import xaero.pac.common.parties.party.member.PartyMemberRank;

public class PartyMemberSnapshot extends PartyInviteSnapshot {

	private final PartyMemberRank rank;

	public PartyMemberSnapshot(String UUID, String username, PartyMemberRank rank) {
		super(UUID, username);
		this.rank = rank;
	}

	public PartyMemberRank getRank() {
		return rank;
	}

}
