package xaero.pac.common.server.player.config.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for a player config on the server side
 */
public interface IPlayerConfigAPI {

    /**
     * Gets the UUID of the player that this config is for.
     *
     * @return the UUID of the config owner, null if wilderness
     */
    @Nullable
    UUID getPlayerId();

    /**
     * Tries to set the value of a specified config option to a specified value.
     * <p>
     * This won't succeed if the specified option is forced to the default config value (by the server mod config)
     * or if the specified value isn't valid for the specified option.
     * In any case, you will receive a {@link SetResult}.
     * <p>
     * All player config option types are statically accessible in {@link PlayerConfigOptions}.
     *
     * @param option the type of the option to set, not null
     * @param value  the value to set the option to, can be null but won't be accepted
     * @param <T>    the type of the option value
     * @return the result type of this action
     */
    @Nonnull
    <T extends Comparable<T>> SetResult tryToSet(@Nonnull IPlayerConfigOptionSpecAPI<T> option, @Nullable T value);

    /**
     * Gets the effective value of a config option.
     * <p>
     * This method calculates the automatic option value if such is used, e.g. the claims color,
     * while {@link #getFromEffectiveConfig} just gets the effective raw value.
     * <p>
     * All player config option types are statically accessible in {@link PlayerConfigOptions}.
     *
     * @param option the type of the option, not null
     * @param <T>    the type of the option value
     * @return the effective value of the option, not null
     */
    @Nonnull
    <T extends Comparable<T>> T getEffective(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

    /**
     * Gets the raw config value from the effective config of a config option, e.g. from the default
     * player config for options that cannot be set per player.
     * <p>
     * This method does not calculate the automatic option value if such is used, e.g. the claims color.
     * <p>
     * All player config option types are statically accessible in {@link PlayerConfigOptions}.
     *
     * @param option the type of the option, not null
     * @param <T>    the type of the option value
     * @return the raw effective value of the option, not null
     */
    @Nonnull
    <T extends Comparable<T>> T getFromEffectiveConfig(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

    /**
     * Gets the raw config value from this config, which can be null in the case of sub-configs.
     *
     * @param o   the type of the option, not null
     * @param <T> the type of the option value
     * @return the raw value of the option, can be null
     */
    @Nullable
    <T extends Comparable<T>> T getRaw(@Nonnull IPlayerConfigOptionSpecAPI<T> o);

    /**
     * Tries to reset the value of a specified config option to the default raw value, as
     * in {@link #getDefaultRawValue}.
     * <p>
     * You will receive a {@link SetResult}.
     * <p>
     * All player config option types are statically accessible in {@link PlayerConfigOptions}.
     *
     * @param option the type of the option to set, not null
     * @param <T>    the type of the option value
     * @return the result type of this action
     */
    @Nonnull
    <T extends Comparable<T>> SetResult tryToReset(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

    /**
     * Gets the type {@link PlayerConfigType} of this config.
     *
     * @return the type of this config, not null
     */
    @Nonnull
    PlayerConfigType getType();

    /**
     * Gets a sub-config of this config from a specified sub-config ID.
     * <p>
     * Gets this config, if the specified sub-config ID is "main".
     *
     * @param id the string ID of the sub-config, not null
     * @return the sub-config, null if it doesn't exist
     */
    @Nullable
    IPlayerConfigAPI getSubConfig(@Nonnull String id);

    /**
     * Gets a sub-config of this config from a specified sub-config ID.
     * <p>
     * Gets this config, if the specified sub-config ID is "main" or isn't used.
     *
     * @param id the string ID of the sub-config, not null
     * @return the effective sub-config, not null
     */
    @Nonnull
    IPlayerConfigAPI getEffectiveSubConfig(@Nonnull String id);

    /**
     * Gets a sub-config of this config from a specified sub-config index.
     * <p>
     * Gets this config, if the specified sub-config index is -1 or isn't used.
     *
     * @param subIndex the sub-config index
     * @return the effective sub-config, not null
     */
    @Nonnull
    IPlayerConfigAPI getEffectiveSubConfig(int subIndex);

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
     * Checks whether a sub-config with a specified index exists.
     * <p>
     * Does not consider "main" a sub-config.
     *
     * @param subIndex the sub-config index
     * @return true, if the sub-config exists, otherwise false
     */
    boolean subConfigExists(int subIndex);

    /**
     * Gets the sub-config currently used for new claims.
     *
     * @return the sub-config, not null
     */
    @Nonnull
    IPlayerConfigAPI getUsedSubConfig();

    /**
     * Gets the server sub-config currently used for new claims.
     *
     * @return the server sub-config, not null
     */
    @Nonnull
    IPlayerConfigAPI getUsedServerSubConfig();

    /**
     * Creates a new sub-config for a specified sub-config ID when possible.
     * <p>
     * The sub-config ID must be unique, at most 16 characters long and
     * consist of A-Z, a-z, 0-9, '-', '_'.
     *
     * @param id the sub-config ID for the creates sub-config
     * @return the newly creates sub-config, null if wasn't successful
     */
    @Nullable
    IPlayerConfigAPI createSubConfig(@Nonnull String id);

    /**
     * Gets the sub-config ID of this config.
     *
     * @return the string sub-config ID of this config, null when not a sub-config
     */
    @Nullable
    String getSubId();

    /**
     * Gets the sub-config index of this config.
     *
     * @return the sub-config index of this config, -1 when not a sub-config
     */
    int getSubIndex();

    /**
     * Gets the number of sub-configs that this config has.
     *
     * @return the number of sub-configs
     */
    int getSubCount();

    /**
     * Gets an unmodifiable list of all string IDs of this config's sub-configs.
     * <p>
     * This must not be a sub-config in itself!
     *
     * @return an unmodifiable {@code List<String>} of sub-config IDs
     */
    @Nonnull
    List<String> getSubConfigIds();

    /**
     * Gets a stream of all sub-configs of this player config.
     *
     * @return the stream of sub-configs, not null
     */
    @Nonnull
    Stream<IPlayerConfigAPI> getSubConfigAPIStream();

    /**
     * Gets the default raw value in this config for an option.
     * <p>
     * This does not redirect to the default player config. It gives you the actual default value
     * for a config option. In the cause of sub-configs, the default raw value is always null.
     * <p>
     * All player config option types are statically accessible in {@link PlayerConfigOptions}.
     *
     * @param option the type of the option, not null
     * @param <T>    the type of the option value
     * @return the default raw option value in this config, always null for sub-configs
     */
    @Nullable
    <T extends Comparable<T>> T getDefaultRawValue(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

    /**
     * Checks whether an option is allowed in this config.
     *
     * @param option the option type, not null
     * @return true, if the option is allowed, otherwise false
     */
    boolean isOptionAllowed(@Nonnull IPlayerConfigOptionSpecAPI<?> option);

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

    /**
     * All possible result types when trying to set an option value
     */
    enum SetResult {
        /**
         * The value is not valid for the option
         */
        INVALID,

        /**
         * The option is not allowed in this config
         */
        ILLEGAL_OPTION,

        /**
         * The option value was reset to the default config's value
         */
        DEFAULTED,

        /**
         * The value was successully set
         */
        SUCCESS
    }

}
