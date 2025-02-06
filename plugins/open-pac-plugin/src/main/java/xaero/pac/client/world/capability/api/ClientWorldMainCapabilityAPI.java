package xaero.pac.client.world.capability.api;

import xaero.pac.client.world.api.IClientWorldDataAPI;

import javax.annotation.Nonnull;

/**
 * API for the main capability attached to a client world
 */
public abstract class ClientWorldMainCapabilityAPI {

	/**
	 * Gets the client world/level data API.
	 *
	 * @return the client world data API instance, not null
	 */
	@Nonnull
	public abstract IClientWorldDataAPI getClientWorldData();

}
