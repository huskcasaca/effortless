package xaero.pac.client.parties.party;

import xaero.pac.common.parties.party.IParty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClientPartyStorage implements IClientPartyStorage<ClientPartyAllyInfo, ClientParty, ClientPartyMemberDynamicInfoSyncableStorage> {

	private String partyName;
	private boolean loading;
	private ClientParty party;
	private int loadingMemberCount;
	private int loadingInviteCount;
	private int loadingAllyCount;
	private int loadingMemberLimit;
	private int loadingInviteLimit;
	private int loadingAllyLimit;
	private final ClientPartyAllyInfoStorage allyInfoStorage;
	private final ClientPartyMemberDynamicInfoSyncableStorage partyMemberDynamicInfoSyncableStorage;

	private ClientPartyStorage(ClientPartyAllyInfoStorage allyInfoStorage, ClientPartyMemberDynamicInfoSyncableStorage partyMemberDynamicInfoSyncableStorage) {
		super();
		this.allyInfoStorage = allyInfoStorage;
		this.partyMemberDynamicInfoSyncableStorage = partyMemberDynamicInfoSyncableStorage;
	}

	@Override
	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	@Override
	public void setLoadingMemberCount(int loadingMemberCount) {
		this.loadingMemberCount = loadingMemberCount;
	}

	@Override
	public void setLoadingAllyCount(int loadingAllyCount) {
		this.loadingAllyCount = loadingAllyCount;
	}

	@Override
	public void setLoadingInviteCount(int loadingInviteCount) {
		this.loadingInviteCount = loadingInviteCount;
	}

	@Override
	public void setMemberLimit(int loadingMemberLimit) {
		this.loadingMemberLimit = loadingMemberLimit;
	}

	@Override
	public void setAllyLimit(int loadingAllyLimit) {
		this.loadingAllyLimit = loadingAllyLimit;
	}

	@Override
	public void setInviteLimit(int loadingInviteLimit) {
		this.loadingInviteLimit = loadingInviteLimit;
	}

	@Override
	public int getUIMemberCount() {
		if(loading || party == null)
			return loadingMemberCount;
		return party.getMemberCount();
	}

	@Override
	public int getUIAllyCount() {
		if(loading || party == null)
			return loadingAllyCount;
		return party.getAllyCount();
	}

	@Override
	public int getUIInviteCount() {
		if(loading || party == null)
			return loadingInviteCount;
		return party.getInviteCount();
	}

	@Override
	public int getMemberLimit() {
		return loadingMemberLimit;
	}

	@Override
	public int getAllyLimit() {
		return loadingAllyLimit;
	}

	@Override
	public int getInviteLimit() {
		return loadingInviteLimit;
	}

	@Override
	public boolean isLoading() {
		return loading;
	}

	@Override
	public void setParty(ClientParty party) {
		this.party = party;
		if(party == null) {
			setPartyName(null);
			allyInfoStorage.clear();
			partyMemberDynamicInfoSyncableStorage.clear();
		}
	}

	@Override
	public void setPartyCast(IParty<?, ?, ?> party) {
		setParty((ClientParty) party);
	}

	@Nullable
	@Override
	public ClientParty getParty() {
		return party;
	}

	@Override
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	@Nullable
	@Override
	public String getPartyName() {
		if((partyName == null || partyName.isEmpty()) && party != null)
			return party.getDefaultName();
		return partyName;
	}

	@Nonnull
	@Override
	public ClientPartyAllyInfoStorage getAllyInfoStorage() {
		return allyInfoStorage;
	}

	@Nonnull
	@Override
	public ClientPartyMemberDynamicInfoSyncableStorage getPartyMemberDynamicInfoSyncableStorage() {
		return this.partyMemberDynamicInfoSyncableStorage;
	}

	public void reset() {
		setParty(null);
	}

	public static final class Builder {

		private Builder() {}

		public ClientPartyStorage build() {
			return new ClientPartyStorage(ClientPartyAllyInfoStorage.Builder.begin().build(), ClientPartyMemberDynamicInfoSyncableStorage.Builder.begin().build());
		}

		public static Builder begin() {
			return new Builder();
		}

	}

}
