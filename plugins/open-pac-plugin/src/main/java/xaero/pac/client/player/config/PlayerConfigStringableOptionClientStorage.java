package xaero.pac.client.player.config;

import net.minecraft.network.chat.Component;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageAPI;
import xaero.pac.common.server.player.config.PlayerConfigOptionSpec;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;
import java.util.function.Function;

public final class PlayerConfigStringableOptionClientStorage<T extends Comparable<T>> extends PlayerConfigOptionClientStorage<T> implements IPlayerConfigStringableOptionClientStorage<T> {

	private final BiPredicate<IPlayerConfigClientStorageAPI, String> stringValidator;

	private PlayerConfigStringableOptionClientStorage(PlayerConfigOptionSpec<T> option, T value, BiPredicate<IPlayerConfigClientStorageAPI, String> stringValidator) {
		super(option, value);
		this.stringValidator = stringValidator;
	}

	@Nonnull
	@Override
	public Function<String, T> getCommandInputParser() {
		return option.getCommandInputParser();
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	@Override
	public Function<Object, Component> getCommandOutputWriterCast() {
		return (Function<Object, Component>) (Object) option.getCommandOutputWriter();
	}

	@Nonnull
	@Override
	public BiPredicate<IPlayerConfigClientStorageAPI, String> getStringValidator(){
		return stringValidator;
	}

	public static final class Builder<T extends Comparable<T>> extends PlayerConfigOptionClientStorage.Builder<T, Builder<T>> {

		@Override
		protected PlayerConfigOptionClientStorage<T> buildInternally() {
			BiPredicate<IPlayerConfigClientStorageAPI, String> stringValidatorPredicate = (c, s) -> {
				T parsedValue;
				try {
					parsedValue = option.getCommandInputParser().apply(s);
				} catch(IllegalArgumentException iae) {
					return false;
				}
				return option.getClientSideValidator().test(c, parsedValue);
			};
			return new PlayerConfigStringableOptionClientStorage<T>(option, value, stringValidatorPredicate);
		}

		@Override
		public PlayerConfigStringableOptionClientStorage<T> build() {
			return (PlayerConfigStringableOptionClientStorage<T>) super.build();
		}

		public static <T extends Comparable<T>> Builder<T> begin(){
			return new Builder<T>().setDefault();
		}

	}

}
