package xaero.pac.common.server.player.config.sync.task;

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
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.config.sub.PlayerSubConfig;
import xaero.pac.common.server.player.config.sync.PlayerConfigSynchronizer;
import xaero.pac.common.server.task.player.ServerPlayerSpreadoutTask;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public final class PlayerConfigSyncSpreadoutTask extends ServerPlayerSpreadoutTask<PlayerConfigSyncSpreadoutTask> {

	private final Deque<IPlayerConfig> configsToSync;
	private Iterator<IPlayerConfig> currentSubIterator;

	private PlayerConfigSyncSpreadoutTask(Deque<IPlayerConfig>configsToSync) {
		this.configsToSync = configsToSync;
	}

	public void addConfigToSync(IPlayerConfig config){
		if(configsToSync.contains(config))
			return;
		configsToSync.addLast(config);
	}

	public boolean stillNeedsSyncing(IPlayerConfig config){
		return configsToSync.contains(config);
	}

	@Override
	public boolean shouldWork(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData, ServerPlayer player) {
		return !configsToSync.isEmpty();
	}

	@Override
	public boolean shouldDrop(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData, ServerPlayer player) {
		return false;
	}

	@Override
	public void onTick(IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData, ServerPlayer player, int perTick) {
		int toSync = perTick;
		while(toSync > 0 && !configsToSync.isEmpty()) {
			PlayerConfigSynchronizer synchronizer = (PlayerConfigSynchronizer) serverData.getPlayerConfigs().getSynchronizer();
			PlayerConfig<?> config = (PlayerConfig<?>) configsToSync.getFirst();

			if (currentSubIterator == null) {
				toSync--;
				synchronizer.sendSyncState(player, config, true);
				synchronizer.syncToClient(player, config, true);
				currentSubIterator = config.getSubConfigIterator();
			}
			while (toSync > 0 && currentSubIterator.hasNext()) {
				toSync--;
				PlayerSubConfig<?> subConfigToSync = (PlayerSubConfig<?>) currentSubIterator.next();
				synchronizer.syncToClient(player, subConfigToSync, true);
			}
			if (!currentSubIterator.hasNext()) {
				synchronizer.sendSyncState(player, config, false);
				configsToSync.removeFirst();
				currentSubIterator = null;
			}
		}
	}

	public static final class Builder {

		private Builder(){
		}

		public Builder setDefault(){
			return this;
		}

		public PlayerConfigSyncSpreadoutTask build(){
			return new PlayerConfigSyncSpreadoutTask(new LinkedList<>());
		}

		public static Builder begin(){
			return new Builder().setDefault();
		}

	}

}
