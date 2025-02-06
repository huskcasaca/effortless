package xaero.pac.client.player.config.api;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.screens.Screen;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

/**
 * API for the player config storage manager on the client side
 */
public interface IPlayerConfigClientStorageManagerAPI {

    /**
     * Gets the read-only "player config" storage for the server claims.
     *
     * @return the server claims config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getServerClaimsConfig();

    /**
     * Gets the read-only "player config" storage for the expired claims.
     *
     * @return the expired claims config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getExpiredClaimsConfig();

    /**
     * Gets the read-only "player config" storage for the wilderness.
     *
     * @return the wilderness config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getWildernessConfig();

    /**
     * Gets the read-only storage for the default player config.
     *
     * @return the default player config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getDefaultPlayerConfig();

    /**
     * Gets the read-only storage for the local client player's config.
     *
     * @return the local player's config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getMyPlayerConfig();

    /**
     * Opens the config GUI screen for the server claims "player config".
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    void openServerClaimsConfigScreen(@Nullable Screen escape, @Nullable Screen parent);

    /**
     * Opens the config GUI screen for the expired claims "player config".
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    void openExpiredClaimsConfigScreen(@Nullable Screen escape, @Nullable Screen parent);

    /**
     * Opens the config GUI screen for the wilderness "player config".
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    void openWildernessConfigScreen(@Nullable Screen escape, @Nullable Screen parent);

    /**
     * Opens the config GUI screen for the default player config.
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    void openDefaultPlayerConfigScreen(@Nullable Screen escape, @Nullable Screen parent);

    /**
     * Opens the config GUI screen for the local client player's config.
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    void openMyPlayerConfigScreen(@Nullable Screen escape, @Nullable Screen parent);

    /**
     * Opens the config GUI screen for the player with a specified username.
     *
     * @param escape     the screen to switch to when the escape key is hit, can be null
     * @param parent     the screen to switch to when the screen is exited normally, can be null
     * @param playerName the username of the player, not null
     */
    void openOtherPlayerConfigScreen(@Nullable Screen escape, @Nullable Screen parent, @Nonnull String playerName);

    /**
     * Gets a stream of all player config option types, including the dynamic ones.
     *
     * @return a stream of all player config options, not null
     */
    @Nonnull
    Stream<IPlayerConfigOptionSpecAPI<?>> getAllOptionsStream();

    /**
     * Gets the option type specification with a specified string option id, including dynamic options.
     * <p>
     * Returns null if no such option exists.
     *
     * @param id the option id, not null
     * @return the option type specification instance, null when doesn't exist
     */
    @Nullable
    IPlayerConfigOptionSpecAPI<?> getOptionForId(@Nonnull String id);

}
