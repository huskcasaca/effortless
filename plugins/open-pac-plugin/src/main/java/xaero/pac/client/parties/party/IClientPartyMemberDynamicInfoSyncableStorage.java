package xaero.pac.client.parties.party;

import xaero.pac.client.parties.party.api.IClientPartyMemberDynamicInfoSyncableStorageAPI;
import xaero.pac.common.parties.party.IPartyMemberDynamicInfoSyncable;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

import java.util.UUID;
import java.util.stream.Stream;

public interface IClientPartyMemberDynamicInfoSyncableStorage
<
	MIS extends IPartyMemberDynamicInfoSyncable
>  extends IClientPartyMemberDynamicInfoSyncableStorageAPI {

	//internal api

	public MIS getOrSetForPlayer(UUID playerId, MIS defaultInfo);
	public boolean removeForPlayer(UUID playerId);
	public void clear();
	public Stream<MIS> getTypedAllStream();

	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPartyMemberDynamicInfoSyncableAPI> getAllStream() {
		return (Stream<IPartyMemberDynamicInfoSyncableAPI>)(Object)getTypedAllStream();
	}
}
