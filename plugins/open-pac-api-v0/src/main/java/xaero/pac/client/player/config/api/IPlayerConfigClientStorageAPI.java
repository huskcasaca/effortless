package xaero.pac.client.player.config.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

/**
 * API for a player config storage on the client side
 */
public interface IPlayerConfigClientStorageAPI {

    /**
     * Gets the config option value storage for a specified config option in this config.
     * <p>
     *
     * @param option the player config option, not null
     * @param <T>    the type of the option value
     * @return the value storage for the config option, not null
     */
    @Nonnull
    <T extends Comparable<T>> IPlayerConfigStringableOptionClientStorageAPI<?> getOptionStorage(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

    /**
     * Gets the type {@link PlayerConfigType} of this config.
     *
     * @return the type of this config, not null
     */
    @Nonnull
    PlayerConfigType getType();

    /**
     * Gets the UUID of the owner of this config.
     *
     * @return the UUID of the owner, null for the wilderness config
     */
    @Nullable
    UUID getOwner();

    /**
     * Gets a stream of all config option value storages for this config.
     *
     * @return the {@link Stream} of all config option value storages, not null
     */
    @Nonnull
    Stream<IPlayerConfigStringableOptionClientStorageAPI<?>> optionStream();

    /**
     * Gets an unmodifiable list of all string IDs of this config's sub-configs.
     *
     * @return an unmodifiable {@code List<String>} of sub-config IDs
     */
    @Nonnull
    List<String> getSubConfigIds();

    /**
     * Gets a sub-config of this config from a specified sub-config ID.
     * <p>
     * Gets this config, if the specified sub-config ID is "main".
     *
     * @param id the string ID of the sub-config, not null
     * @return the sub-config, null if it doesn't exist
     */
    @Nullable
    IPlayerConfigClientStorageAPI getSubConfig(@Nonnull String id);

    /**
     * Gets a sub-config of this config from a specified sub-config ID.
     * <p>
     * Gets this config, if the specified sub-config ID is "main" or isn't used.
     *
     * @param id the string ID of the sub-config, not null
     * @return the effective sub-config, not null
     */
    @Nonnull
    IPlayerConfigClientStorageAPI getEffectiveSubConfig(@Nonnull String id);

    /**
     * Checks whether a sub-config with a specified string ID exists.
     * <p>
     * Does not consider "main" a sub-config.
     *
     * @param id the string ID of the sub-config, not null
     * @return true, if the sub-config exists, otherwise false
     */
    boolean subConfigExists(@Nonnull String id);

    /**
     * Gets the number of sub-configs that this config has.
     *
     * @return the number of sub-configs
     */
    int getSubCount();

    /**
     * Gets a stream of all sub-configs of this player config.
     * <p>
     * This must not be a sub-config in itself.
     *
     * @return a stream of all sub-configs, not null
     */
    @Nonnull
    Stream<IPlayerConfigClientStorageAPI> getSubConfigAPIStream();

    /**
     * Checks whether this player (sub-)config is in the process of being deleted.
     * <p>
     * This is typically only ever true for sub-configs.
     *
     * @return true if this player (sub-)config is in the process of being deleted, otherwise false
     */
    boolean isBeingDeleted();

    /**
     * Gets the maximum number of sub-configs that this player config is allowed to have.
     * <p>
     * Returns 0 if this is a sub-config. Returns the maximum int value if this is a server claims config.
     *
     * @return the sub-config limit
     */
    int getSubConfigLimit();

}
