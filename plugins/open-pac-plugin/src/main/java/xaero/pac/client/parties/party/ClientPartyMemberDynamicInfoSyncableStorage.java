package xaero.pac.client.parties.party;

import xaero.pac.common.parties.party.PartyMemberDynamicInfoSyncable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class ClientPartyMemberDynamicInfoSyncableStorage implements IClientPartyMemberDynamicInfoSyncableStorage<PartyMemberDynamicInfoSyncable> {

	private Map<UUID, PartyMemberDynamicInfoSyncable> storage;

	private ClientPartyMemberDynamicInfoSyncableStorage(Map<UUID, PartyMemberDynamicInfoSyncable> storage) {
		super();
		this.storage = storage;
	}

	@Override
	public PartyMemberDynamicInfoSyncable getForPlayer(@Nonnull UUID playerId) {
		return storage.get(playerId);
	}

	public PartyMemberDynamicInfoSyncable getOrSetForPlayer(UUID playerId, PartyMemberDynamicInfoSyncable defaultInfo) {
		return storage.computeIfAbsent(playerId, i -> defaultInfo == null ? new PartyMemberDynamicInfoSyncable(i, true) : defaultInfo);
	}

	@Override
	public boolean removeForPlayer(UUID playerId) {
		return storage.remove(playerId) != null;
	}

	@Override
	public Stream<PartyMemberDynamicInfoSyncable> getTypedAllStream() {
		return storage.values().stream();
	}

	@Override
	public void clear() {
		storage.clear();
	}

	public static final class Builder {

		private Builder() {}

		public ClientPartyMemberDynamicInfoSyncableStorage build() {
			return new ClientPartyMemberDynamicInfoSyncableStorage(new HashMap<>());
		}

		public static Builder begin() {
			return new Builder();
		}

	}

}
