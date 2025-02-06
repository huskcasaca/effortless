package xaero.pac.client.world.capability.api;

import xaero.pac.client.world.capability.ClientWorldCapabilityProvider;
import xaero.pac.common.capability.ICapability;
import xaero.pac.common.capability.ICapabilityType;
import xaero.pac.common.capability.api.ICapabilityHelperAPI;

/**
 * Client world capability types to use as an argument in {@link ICapabilityHelperAPI#getCapability}
 */
public class ClientWorldCapabilityTypes {

	/** The main capability type for client worlds */
	public static ICapability<ClientWorldMainCapabilityAPI> MAIN_CAP = (ICapabilityType<ClientWorldMainCapabilityAPI>) ClientWorldCapabilityProvider::new;

}
