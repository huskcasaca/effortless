package xaero.pac.client.parties.party;

import xaero.pac.client.parties.party.api.IClientPartyAllyInfoStorageAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IClientPartyAllyInfoStorage<A extends IClientPartyAllyInfo> extends IClientPartyAllyInfoStorageAPI {

	//internal api


	@Nullable
	@Override
	A get(@Nonnull UUID id);
	public void remove(UUID id);
	public void add(A ally);
	public void clear();

}
