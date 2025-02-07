package xaero.pac.common.server.player.config.api;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;

/**
 * All possible player config types
 */
public enum PlayerConfigType {

    /**
     * Server claims config
     */
    SERVER(null),

    /**
     * Expired claims config
     */
    EXPIRED(null),

    /**
     * Wilderness config
     */
    WILDERNESS(null),

    /**
     * The default player config
     */
    DEFAULT_PLAYER(null),

    /**
     * A player config
     */
    PLAYER(null);

    private final Component name;

    PlayerConfigType(Component name) {
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
