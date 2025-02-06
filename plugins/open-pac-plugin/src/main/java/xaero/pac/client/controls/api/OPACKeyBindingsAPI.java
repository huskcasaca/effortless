package xaero.pac.client.controls.api;

import net.minecraft.client.KeyMapping;

import javax.annotation.Nonnull;

/**
 * API for the mod's keybindings
 */
public interface OPACKeyBindingsAPI {

	/**
	 * Gets the keybinding used for opening the main mod menu.
	 *
	 * @return the keybinding, not null
	 */
	@Nonnull
	public KeyMapping getOpenModMenuKeyBinding();

}

