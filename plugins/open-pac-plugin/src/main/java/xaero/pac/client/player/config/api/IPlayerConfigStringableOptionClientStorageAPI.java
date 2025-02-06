package xaero.pac.client.player.config.api;

import net.minecraft.network.chat.Component;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * API for a stringable player config option value storage on the client side
 */
public interface IPlayerConfigStringableOptionClientStorageAPI<T extends Comparable<T>> extends IPlayerConfigOptionClientStorageAPI<T> {

	@Override
	@Nonnull
	public IPlayerConfigOptionSpecAPI<T> getOption();

	@Override
	@Nonnull
	public String getId();

	@Override
	@Nonnull
	public String getComment();

	@Override
	@Nonnull
	public String getTranslation();

	@Override
	@Nonnull
	public Class<T> getType();

	@Override
	@Nullable
	public T getValue();

	@Override
	@Nonnull
	public BiPredicate<IPlayerConfigClientStorageAPI, T> getValidator();

	@Nullable
	@Override
	public String getTooltipPrefix();

	@Override
	public boolean isDefaulted();

	@Override
	public boolean isMutable();

	/**
	 * Gets the string input parser for this option.
	 * <p>
	 * It is the same one that is used for parsing command inputs.
	 *
	 * @return the string input parser function, not null
	 */
	@Nonnull
	public Function<String, T> getCommandInputParser();

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
	public Function<Object, Component> getCommandOutputWriterCast();

	/**
	 * Gets the string value input validator for this option.
	 *
	 * @return the string value input validator function, not null
	 */
	@Nonnull
	public BiPredicate<IPlayerConfigClientStorageAPI, String> getStringValidator();

}
