package xaero.pac.client.world.capability;

import xaero.pac.client.world.ClientWorldData;
import xaero.pac.client.world.IClientWorldData;
import xaero.pac.client.world.capability.api.ClientWorldMainCapabilityAPI;

import javax.annotation.Nonnull;

public class ClientWorldMainCapability extends ClientWorldMainCapabilityAPI {

	//internal API

	protected final ClientWorldData clientWorldData;

	public ClientWorldMainCapability(ClientWorldData clientWorldData) {
		this.clientWorldData = clientWorldData;
	}

	public IClientWorldData
	getClientWorldDataInternal() {
		return (IClientWorldData) clientWorldData;
	}

	public ClientWorldData getClientWorldDataSuperInternal() {
		return clientWorldData;
	}

	@Nonnull
	@Override
	public IClientWorldData getClientWorldData() {
		return clientWorldData;
	}

}
