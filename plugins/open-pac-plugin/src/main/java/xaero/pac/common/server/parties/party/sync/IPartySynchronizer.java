package xaero.pac.common.server.parties.party.sync;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.parties.party.IServerParty;

public interface IPartySynchronizer
<
	P extends IServerParty<?, ?, ?>
> {

	//internal api

	void syncToClient(ServerPlayer player, P party);
	void syncToPartyAndAlliersUpdateName(P party, String value);
	public IPartyMemberDynamicInfoSynchronizer<P> getOftenSyncedInfoSync();
	public void onServerTick();
	public void onLazyPacketsDropped(ServerPlayer player);
}
