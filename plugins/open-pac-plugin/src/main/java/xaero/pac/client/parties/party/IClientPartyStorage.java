package xaero.pac.client.parties.party;

import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.common.parties.party.IParty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IClientPartyStorage
<
	A extends IClientPartyAllyInfo,
	P extends IClientParty<?,?,?>,
	MISS extends IClientPartyMemberDynamicInfoSyncableStorage<?>
> extends IClientPartyStorageAPI {

	//internal api

	@Nullable
	@Override
	public P getParty();

	public void setParty(P party);

	public void setPartyCast(IParty<?,?,?> party);

	public void setPartyName(String partyName);

	@Nonnull
	@Override
	public IClientPartyAllyInfoStorage<A> getAllyInfoStorage();

	@Nonnull
	@Override
	public MISS getPartyMemberDynamicInfoSyncableStorage();

	public void setLoading(boolean loading);

	public void setLoadingMemberCount(int loadingMemberCount);

	public void setLoadingAllyCount(int loadingAllyCount);

	public void setLoadingInviteCount(int loadingInviteCount);

	public void setMemberLimit(int memberLimit);

	public void setAllyLimit(int allyLimit);

	public void setInviteLimit(int inviteLimit);

}
