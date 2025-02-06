package xaero.pac.common.platform.services;

import xaero.pac.client.controls.keybinding.IKeyBindingHelper;
import xaero.pac.common.entity.IEntityAccess;
import xaero.pac.common.reflect.IMappingHelper;
import xaero.pac.common.server.world.IServerChunkCacheAccess;

import java.nio.file.Path;

public interface IPlatformHelper {

	/**
	 * Gets the name of the current platform
	 *
	 * @return The name of the current platform.
	 */
	String getPlatformName();

	/**
	 * Checks if a mod with the given id is loaded.
	 *
	 * @param modId The mod to check if it is loaded.
	 * @return True if the mod is loaded, false otherwise.
	 */
	boolean isModLoaded(String modId);

	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	boolean isDevelopmentEnvironment();

	IKeyBindingHelper getKeyBindingHelper();

	IServerChunkCacheAccess getServerChunkCacheAccess();

	IEntityAccess getEntityAccess();

	IMappingHelper getMappingHelper();

	Path getDefaultConfigFolder();

}
