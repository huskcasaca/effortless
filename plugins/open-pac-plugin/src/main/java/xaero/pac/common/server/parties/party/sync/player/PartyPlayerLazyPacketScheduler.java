package xaero.pac.common.server.parties.party.sync.player;

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
import xaero.pac.common.server.lazypacket.task.schedule.LazyPacketScheduleTask;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.parties.party.sync.PartySynchronizer;

public abstract class PartyPlayerLazyPacketScheduler extends LazyPacketScheduleTask {

	protected final PartySynchronizer synchronizer;

	protected PartyPlayerLazyPacketScheduler(PartySynchronizer synchronizer) {
		this.synchronizer = synchronizer;
	}

	@Override
	public boolean shouldDrop(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData, ServerPlayer holder) {
		return false;
	}

}
