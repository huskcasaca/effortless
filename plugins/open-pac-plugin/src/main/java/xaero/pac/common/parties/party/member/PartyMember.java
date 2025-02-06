package xaero.pac.common.parties.party.member;

import xaero.pac.common.parties.party.PartyPlayerInfo;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PartyMember extends PartyPlayerInfo<PartyMember> implements IPartyMember, Comparable<PartyMember> {

	private final static int OWNER_ORDINAL = PartyMemberRank.values().length;

	private PartyMemberRank rank;
	private final boolean owner;

	public PartyMember(UUID playerUUID, boolean owner) {
		super(playerUUID);
		this.rank = PartyMemberRank.MEMBER;
		this.owner = owner;
	}

	public void setRank(PartyMemberRank rank) {
		this.rank = rank;
	}

	@Override
	public boolean isOwner() {
		return owner;
	}

	@Nonnull
	@Override
	public PartyMemberRank getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", getUUID(), getUsername(), getRank());
	}

	@Override
	public int compareTo(PartyMember o) {
		int thisRankNumber = owner ? OWNER_ORDINAL : rank.ordinal();
		int otherRankNumber = o.owner ? OWNER_ORDINAL : o.rank.ordinal();
		if(thisRankNumber != otherRankNumber)
			return thisRankNumber > otherRankNumber ? -1 : 1;
		return getUsername().compareTo(o.getUsername());
	}
}
