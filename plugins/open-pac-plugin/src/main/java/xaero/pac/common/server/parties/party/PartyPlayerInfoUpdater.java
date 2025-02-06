package xaero.pac.common.server.parties.party;

import com.mojang.authlib.GameProfile;
import xaero.pac.common.parties.party.member.IPartyMember;

public final class PartyPlayerInfoUpdater {

	public <M extends IPartyMember> void update(IServerParty<M,?,?> party, M pi, GameProfile playerProfile) {
		party.updateUsername(pi, playerProfile.getName());
	}

}
