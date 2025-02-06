package xaero.pac.common.packet.parties;

import net.minecraft.nbt.CompoundTag;
import xaero.pac.common.parties.party.PartyPlayerInfo;
import xaero.pac.common.parties.party.member.PartyInvite;
import xaero.pac.common.parties.party.member.PartyMember;
import xaero.pac.common.parties.party.member.PartyMemberRank;

import java.util.UUID;

public class PartyPlayerInfoCodec {

	public PartyInvite fromPartyInviteTag(CompoundTag playerInfoTag) {
		if(playerInfoTag.isEmpty())
			return null;
		try {
			UUID playerUUID = playerInfoTag.getUUID("i");
			String username = playerInfoTag.getString("n");
			if(username.isEmpty() || username.length() > 128)
				return null;
			PartyInvite result = new PartyInvite(playerUUID);
			result.setUsername(username);
			return result;
		} catch(Throwable t) {
			return null;
		}
	}

	public PartyMember fromMemberTag(CompoundTag memberTag, boolean isOwner) {
		if(memberTag.isEmpty())
			return null;
		try {
			UUID playerUUID = memberTag.getUUID("i");
			String username = memberTag.getString("n");
			if(username.isEmpty() || username.length() > 128) {
				return null;
			}
			String rank = memberTag.getString("r");
			if(rank.isEmpty() || rank.length() > 128) {
				return null;
			}
			PartyMember result = new PartyMember(playerUUID, isOwner);
			result.setUsername(username);
			result.setRank(PartyMemberRank.valueOf(rank));
			return result;
		} catch(Throwable t) {
			return null;
		}
	}

	private CompoundTag toPlayerInfoTag(PartyPlayerInfo<?> playerInfo) {
		CompoundTag infoTag = new CompoundTag();
		infoTag.putUUID("i", playerInfo.getUUID());
		infoTag.putString("n", playerInfo.getUsername());
		return infoTag;
	}

	public CompoundTag toPartyInviteTag(PartyInvite playerInfo) {
		return toPlayerInfoTag(playerInfo);
	}

	public CompoundTag toMemberTag(PartyMember member) {
		CompoundTag memberTag = toPlayerInfoTag(member);
		memberTag.putString("r", member.getRank().toString());
		return memberTag;
	}

}
