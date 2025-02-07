package xaero.pac.client.controls.api;

import javax.annotation.Nonnull;

import net.minecraft.client.KeyMapping;

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
    KeyMapping getOpenModMenuKeyBinding();

}

