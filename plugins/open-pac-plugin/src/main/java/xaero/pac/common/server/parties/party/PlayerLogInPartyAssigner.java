package xaero.pac.common.server.parties.party;

import net.minecraft.world.entity.player.Player;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;

public final class PlayerLogInPartyAssigner {

	public void assign(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>
							   serverData, Player player, PartyPlayerInfoUpdater partyMemberInfoUpdater) {
		IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly> playerParty = serverData.getPartyManager().getPartyByMember(player.getUUID());
		if(playerParty != null) {
			partyMemberInfoUpdater.update(playerParty, playerParty.getMemberInfo(player.getUUID()), player.getGameProfile());
			playerParty.registerActivity(serverData.getServerInfo());
		}
	}

}
