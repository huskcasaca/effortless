package xaero.pac.common.server.parties.party.io.serialization.nbt.member;

import net.minecraft.nbt.CompoundTag;
import xaero.pac.common.parties.party.member.PartyInvite;

public class PartyInviteNbtSerializer {

	public CompoundTag serialize(PartyInvite info) {
		CompoundTag nbt = new CompoundTag();
		nbt.putUUID("uuid", info.getUUID());
		nbt.putString("username", info.getUsername());
		return nbt;
	}

	public PartyInvite deserialize(CompoundTag nbt) {
		PartyInvite result = new PartyInvite(nbt.getUUID("uuid"));
		result.setUsername(nbt.getString("username"));
		return result;
	}

}
