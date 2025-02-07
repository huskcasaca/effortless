package xaero.pac.client.player.config.api;

import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

/**
 * API for a stringable player config option value storage on the client side
 */
public interface IPlayerConfigStringableOptionClientStorageAPI<T extends Comparable<T>> extends IPlayerConfigOptionClientStorageAPI<T> {

    @Override
    @Nonnull
    IPlayerConfigOptionSpecAPI<T> getOption();

    @Override
    @Nonnull
    String getId();

    @Override
    @Nonnull
    String getComment();

    @Override
    @Nonnull
    String getTranslation();

    @Override
    @Nonnull
    Class<T> getType();

    @Override
    @Nullable
    T getValue();

    @Override
    @Nonnull
    BiPredicate<IPlayerConfigClientStorageAPI, T> getValidator();

    @Nullable
    @Override
    String getTooltipPrefix();

    @Override
    boolean isDefaulted();

    @Override
    boolean isMutable();

    /**
     * Gets the string input parser for this option.
     * <p>
     * It is the same one that is used for parsing command inputs.
     *
     * @return the string input parser function, not null
     */
    @Nonnull
    Function<String, T> getCommandInputParser();

    /**
     * Gets the string output writer for this option.
     * <p>
     * It is the same one that is used for displaying option values in command outputs.
     * <p>
     * It accepts values of any type but will only work with the right one.
     *
     * @return the string output writer function, not null
     */
    @Nonnull
    Function<Object, Component> getCommandOutputWriterCast();

    /**
     * Gets the string value input validator for this option.
     *
     * @return the string value input validator function, not null
     */
    @Nonnull
    BiPredicate<IPlayerConfigClientStorageAPI, String> getStringValidator();

}
