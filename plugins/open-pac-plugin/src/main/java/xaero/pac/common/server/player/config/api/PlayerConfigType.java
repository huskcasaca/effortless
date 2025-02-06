package xaero.pac.common.server.player.config.api;

import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

/**
 * All possible player config types
 */
public enum PlayerConfigType {

	/** Server claims config */
	SERVER(Component.translatable("gui.xaero_pac_config_type_server")),

	/** Expired claims config */
	EXPIRED(Component.translatable("gui.xaero_pac_config_type_expired")),

	/** Wilderness config */
	WILDERNESS(Component.translatable("gui.xaero_pac_config_type_wilderness")),

	/** The default player config */
	DEFAULT_PLAYER(Component.translatable("gui.xaero_pac_config_type_default_player")),

	/** A player config */
	PLAYER(Component.translatable("gui.xaero_pac_config_type_player"));

	private final Component name;

	PlayerConfigType(Component name){
		this.name = name;
	}

	/**
	 * Gets the display name of the config type.
	 *
	 * @return the display name of the config type, not null
	 */
	@Nonnull
	public Component getName() {
		return name;
	}
}
