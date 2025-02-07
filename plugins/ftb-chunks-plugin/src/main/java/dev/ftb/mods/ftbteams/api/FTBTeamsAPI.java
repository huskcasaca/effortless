package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import dev.ftb.mods.ftbteams.api.event.TeamManagerEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBTeamsAPI {
	public static final String MOD_ID = "ftbteams";
	public static final String MOD_NAME = "FTB Teams";

	private static API instance;

	/**
	 * Retrieve the public API instance.
	 *
	 * @return the API handler
	 */
	public static API api() {
		return instance;
	}

	/**
	 * Convenience method to get a resource location in the FTB Teams namespace
	 *
	 * @param path the resource location path component
	 * @return a new resource location
	 */
	public static ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	/**
	 * Do not call this method yourself!
	 * @param instance the API instance
	 */
	@ApiStatus.Internal
	public static void _init(API instance) {
		if (FTBTeamsAPI.instance != null) {
			throw new IllegalStateException("can't init more than once!");
		}
		FTBTeamsAPI.instance = instance;
	}

	/**
	 * Top-level API. Retrieve an instance of this via {@link FTBTeamsAPI#api()}.
	 */
	public interface API {
		/**
		 * Check if the server-side team manager is loaded. This will always be the case on the server once the
		 * {@link TeamManagerEvent#CREATED} Architectury event has been fired (fired when the server is about to start).
		 *
		 * @return true if the team manager exists, false otherwise
		 */
		boolean isManagerLoaded();

		/**
		 * Get the server-side team manager instance.
		 *
		 * @return the team manager
		 * @throws NullPointerException if the manager is not yet loaded, or this is called from the client
		 */
		TeamManager getManager();

		/**
		 * Check if the client-side team manager is loaded. This will true once the client has received the sync'd
		 * server-side data from the server, which occurs when the player connects.
		 *
		 * @return true if the client team manager exists, false otherwise
		 */
		boolean isClientManagerLoaded();

		/**
		 * Get the client-side team manager instance.
		 *
		 * @return the client team manager
		 * @throws NullPointerException if the manager is not yet loaded, or this is called from the client
		 */
		ClientTeamManager getClientManager();

		/**
		 * Set a custom handler for creating parties via the GUI, i.e. when the "Create Party" button is clicked in
		 * the player's team GUI. The presence of a custom creation handler also prevents parties from being created
		 * via the API.
		 *
		 * @param handler the new handler to use
		 * @return the previous handler
		 * @deprecated use {@link #setPartyCreationFromAPIOnly(boolean)}
		 */
		@Deprecated
		@Nullable
		CustomPartyCreationHandler setCustomPartyCreationHandler(@Nullable CustomPartyCreationHandler handler);

		/**
		 * Get the custom party creation handler in effect, if any
		 * @return the current custom handler, or null if none is in effect
		 * @deprecated see {@link #setPartyCreationFromAPIOnly(boolean)}
		 */
		@Deprecated
		@Nullable
		CustomPartyCreationHandler getCustomPartyCreationHandler();

		/**
		 * By setting api-only team creation to true, this prevents players from creating party teams using either the
		 * command interface or GUI interface of FTB Teams. Parties will only be creatable by other mods via the
		 * FTB Teams API. This is intended for use with custom modpacks which link party creation with e.g. base
		 * creation.
		 * <p>
		 * By default, FTB Teams allows party creation via all methods.
		 *
		 * @param apiOnly true to allow party creation via API only, false to allow normal creation
		 */
		void setPartyCreationFromAPIOnly(boolean apiOnly);

		/**
		 * Create a team message with a timestamp of the current system time.
		 * @param sender the sending player
		 * @param text the message content
		 * @return a new TeamMessage object
		 */
		TeamMessage createMessage(UUID sender, Component text);
	}
}
