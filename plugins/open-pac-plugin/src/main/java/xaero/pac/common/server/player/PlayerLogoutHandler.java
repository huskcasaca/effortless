package xaero.pac.common.server.player;

import net.minecraft.server.level.ServerPlayer;
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
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.parties.party.sync.IPartyMemberDynamicInfoSynchronizer;

public class PlayerLogoutHandler {

	public void handle(ServerPlayer player, IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>
			serverData) {
		serverData.getForceLoadManager().updateTicketsFor(serverData.getPlayerConfigs(), player.getUUID(), true);
		//PlayerMainCapability playerMainCap = (PlayerMainCapability) player.getCapability(PlayerCapabilityProvider.MAIN_CAP).orElse(null);
		IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly> playerParty = serverData.getPartyManager().getPartyByMember(player.getUUID());
		if(playerParty != null) {
			playerParty.registerActivity(serverData.getServerInfo());
			IPartyMemberDynamicInfoSynchronizer<IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> partyOftenSyncedSync = serverData.getPartyManager().getPartySynchronizer().getOftenSyncedInfoSync();
			partyOftenSyncedSync.handlePlayerLeave(playerParty, player);
		}
		if(serverData.getServerClaimsManager().hasPlayerInfo(player.getUUID())) {
			serverData.getServerClaimsManager().getPlayerInfo(player.getUUID()).registerActivity(serverData.getServerInfo());
		}

		serverData.getServerTickHandler().getLazyPacketSender().clearForPlayer(player);
	}

}
