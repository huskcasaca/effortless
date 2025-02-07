package xaero.pac.common.server.player.data.api;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerPlayer;

/**
 * API for data attached to a server player
 */
public abstract class ServerPlayerDataAPI {

    /**
     * Checks if the player is using the claims admin mode.
     *
     * @return true if the player is in the claims admin mode, otherwise false
     */
    public abstract boolean isClaimsAdminMode();

    /**
     * Checks if the player is using the claims non-ally mode.
     *
     * @return true if the player is in the claims non-ally mode, otherwise false
     */
    public abstract boolean isClaimsNonallyMode();

    /**
     * Checks if the player is using the server claim mode.
     *
     * @return true if the player is in the server claim mode, otherwise false
     */
    public abstract boolean isClaimsServerMode();

    /**
     * Gets the player data for a specified logged in player.
     *
     * @param player the player, not null
     * @return the parties and claims player data for the player, not null
     */
    @Nonnull
    public static ServerPlayerDataAPI from(@Nonnull ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

}
