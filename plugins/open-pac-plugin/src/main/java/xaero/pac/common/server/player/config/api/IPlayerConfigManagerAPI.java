package xaero.pac.common.server.player.config.api;

import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for the player config manager on the server side
 */
public interface IPlayerConfigManagerAPI {

    /**
     * Gets or creates the player config for the player with a specified UUID.
     * <p>
     * Gets the wilderness config if the specified UUID is null,
     * the server claims config if the UUID is {@link PlayerConfig#SERVER_CLAIM_UUID},
     * the expired claims config if the UUID is {@link PlayerConfig#EXPIRED_CLAIM_UUID}.
     *
     * @param id the UUID of the player, null for wilderness
     * @return the player config instance, not null
     */
    @Nonnull
    IPlayerConfigAPI getLoadedConfig(@Nullable UUID id);

    /**
     * Gets the default player config instance.
     *
     * @return the default player config instance, not null
     */
    @Nonnull
    IPlayerConfigAPI getDefaultConfig();

    /**
     * Gets the wilderness config instance.
     *
     * @return the wilderness config instance, not null
     */
    @Nonnull
    IPlayerConfigAPI getWildernessConfig();

    /**
     * Gets the server claims config instance.
     *
     * @return the server claims config instance, not null
     */
    @Nonnull
    IPlayerConfigAPI getServerClaimConfig();

    /**
     * Gets the expired claims config instance.
     *
     * @return the expired claims config instance, not null
     */
    @Nonnull
    IPlayerConfigAPI getExpiredClaimConfig();

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
