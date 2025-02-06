package xaero.pac.client.parties.party;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientPartyAllyInfoStorage implements IClientPartyAllyInfoStorage<ClientPartyAllyInfo> {

	private final Map<UUID, ClientPartyAllyInfo> cache;

	private ClientPartyAllyInfoStorage(Map<UUID, ClientPartyAllyInfo> cache) {
		super();
		this.cache = cache;
	}

	@Nullable
	@Override
	public ClientPartyAllyInfo get(@Nonnull UUID id) {
		return cache.get(id);
	}

	public void add(ClientPartyAllyInfo info) {
		cache.put(info.getAllyId(), info);
	}

	@Override
	public void remove(UUID id) {
		cache.remove(id);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	public static final class Builder {

		private Builder() {}

		public ClientPartyAllyInfoStorage build() {
			return new ClientPartyAllyInfoStorage(new HashMap<>());
		}

		public static Builder begin() {
			return new Builder();
		}

	}

}
