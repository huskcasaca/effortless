package xaero.pac.common.server.parties.party.sync;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.parties.party.IPartyMemberDynamicInfoSyncable;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.data.ServerPlayerData;

public interface IPartyMemberDynamicInfoSynchronizer
<
	P extends IServerParty<?, ?, ?>
> {

	//internal api

	void syncToPartyDynamicInfo(P party, IPartyMemberDynamicInfoSyncable syncedInfo, P fromParty);

	void syncToPartyMutualAlliesDynamicInfo(P party, IPartyMemberDynamicInfoSyncable syncedInfo);

	void syncToClientAllDynamicInfo(ServerPlayer onlinePlayer, P party, boolean b);

	void syncToClientMutualAlliesDynamicInfo(ServerPlayer onlinePlayer, P party, boolean b);

	void handlePlayerLeave(P playerParty, ServerPlayer player);

	void syncToPartiesDynamicInfo(P playerParty, IPartyMemberDynamicInfoSyncable oftenSyncedPartyMemberInfo);

	void onPlayerTick(ServerPlayerData mainCap, ServerPlayer player);

}
