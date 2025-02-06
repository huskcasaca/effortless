package xaero.pac.common.server.player.config;

import net.minecraft.network.chat.Component;
import xaero.pac.client.player.config.PlayerConfigClientStorage;
import xaero.pac.common.packet.config.ClientboundPlayerConfigDynamicOptionsPacket;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PlayerConfigStringOptionSpec extends PlayerConfigOptionSpec<String> {

	private final int maxLength;

	private PlayerConfigStringOptionSpec(Class<String> type, String id, String shortenedId, List<String> path, String defaultValue, BiFunction<PlayerConfig<?>, String, String> defaultReplacer, String comment,
										 String translation, String[] translationArgs, String commentTranslation, String[] commentTranslationArgs, PlayerConfigOptionCategory category, Function<String, String> commandInputParser, Function<String, Component> commandOutputWriter,
										 BiPredicate<PlayerConfig<?>, String> serverSideValidator, BiPredicate<PlayerConfigClientStorage, String> clientSideValidator, int maxLength, String tooltipPrefix, Predicate<PlayerConfigType> configTypeFilter, ClientboundPlayerConfigDynamicOptionsPacket.OptionType syncOptionType, boolean dynamic) {
		super(type, id, shortenedId, path, defaultValue, defaultReplacer, comment, translation, translationArgs, commentTranslation, commentTranslationArgs, category, commandInputParser, commandOutputWriter, serverSideValidator, clientSideValidator, tooltipPrefix, configTypeFilter, syncOptionType, dynamic);
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public final static class Builder extends PlayerConfigOptionSpec.Builder<String, Builder> {

		private int maxLength;

		protected Builder() {
			super(String.class);
		}

		@Override
		public Builder setDefault() {
			setMaxLength(32);
			return super.setDefault();
		}

		public Builder setMaxLength(int maxLength) {
			this.maxLength = maxLength;
			return this;
		}

		public static <T> Builder begin(){
			return new Builder().setDefault();
		}

		@Override
		protected Predicate<String> buildValueValidator() {
			Predicate<String> normalValidator = super.buildValueValidator();
			return v -> {
				if(!normalValidator.test(v))
					return false;
				return v.length() <= maxLength;
			};
		}

		@Override
		public PlayerConfigStringOptionSpec build(Map<String, PlayerConfigOptionSpec<?>> dest) {
			if(tooltipPrefix == null)
				tooltipPrefix = String.format("~%s", maxLength);
			return (PlayerConfigStringOptionSpec) super.build(dest);
		}

		@Override
		protected PlayerConfigStringOptionSpec buildInternally(List<String> path, String shortenedId, Function<String, String> commandInputParser) {
			return new PlayerConfigStringOptionSpec(type, id, shortenedId, path, defaultValue, defaultReplacer, comment, translation, translationArgs, commentTranslation, commentTranslationArgs, category, commandInputParser, commandOutputWriter, serverSideValidator, clientSideValidator, maxLength, tooltipPrefix, configTypeFilter, ClientboundPlayerConfigDynamicOptionsPacket.OptionType.STRING, dynamic);
		}

	}

}
