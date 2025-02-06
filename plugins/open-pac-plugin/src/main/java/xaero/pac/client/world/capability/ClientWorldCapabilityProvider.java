package xaero.pac.client.world.capability;

import net.minecraft.client.multiplayer.ClientLevel;
import xaero.pac.client.world.ClientWorldData;
import xaero.pac.client.world.capability.api.ClientWorldCapabilityTypes;
import xaero.pac.common.capability.ICapability;
import xaero.pac.common.capability.ICapabilityProvider;
import xaero.pac.common.capability.ICapableObject;

public class ClientWorldCapabilityProvider implements ICapabilityProvider {

	protected final ClientWorldMainCapability mainCapability;

	public ClientWorldCapabilityProvider(ICapableObject capableObject) {
		if(!(capableObject instanceof ClientLevel)) {
			this.mainCapability = null;
			return;
		}
		this.mainCapability = new ClientWorldMainCapability(ClientWorldData.Builder.begin().build());
	}

	@SuppressWarnings("unchecked")
	public <T> T getCapability(ICapability<T> cap) {
		if(cap == ClientWorldCapabilityTypes.MAIN_CAP)
			return (T) mainCapability;
		return null;
	}

}
