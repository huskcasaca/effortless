package xaero.pac.common.server.parties.party.io.serialization.nbt.member;

import net.minecraft.nbt.CompoundTag;
import xaero.pac.common.parties.party.member.PartyMember;
import xaero.pac.common.parties.party.member.PartyMemberRank;

public class PartyMemberNbtSerializer {

	public CompoundTag serialize(PartyMember info) {
		CompoundTag nbt = new CompoundTag();
		nbt.putUUID("uuid", info.getUUID());
		nbt.putString("username", info.getUsername());
		nbt.putString("rank", info.getRank().toString());
		return nbt;
	}

	public PartyMember deserialize(CompoundTag nbt, boolean isOwner) {
		PartyMember result = new PartyMember(nbt.getUUID("uuid"), isOwner);
		result.setUsername(nbt.getString("username"));
		result.setRank(PartyMemberRank.valueOf(nbt.getString("rank")));
		return result;
	}

}
