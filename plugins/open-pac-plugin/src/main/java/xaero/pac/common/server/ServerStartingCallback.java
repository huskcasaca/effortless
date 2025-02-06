package xaero.pac.common.server;

import net.minecraft.server.MinecraftServer;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.claims.player.io.PlayerClaimInfoManagerIO;
import xaero.pac.common.server.config.ServerConfig;
import xaero.pac.common.server.parties.party.IServerParty;

public class ServerStartingCallback {

	private final PlayerClaimInfoManagerIO<?> playerClaimInfoManagerIO;

	public ServerStartingCallback(PlayerClaimInfoManagerIO<?> playerClaimInfoManagerIO) {
		super();
		this.playerClaimInfoManagerIO = playerClaimInfoManagerIO;
	}

	public void onLoad(MinecraftServer server) {
		playerClaimInfoManagerIO.load();
		IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = ServerData.from(server);
		serverData.getPlayerPermissionSystemManager().updateUsedSystem(ServerConfig.CONFIG.permissionSystem.get());
		serverData.getPlayerPartySystemManager().updatePrimarySystem(ServerConfig.CONFIG.primaryPartySystem.get());
	}

}
