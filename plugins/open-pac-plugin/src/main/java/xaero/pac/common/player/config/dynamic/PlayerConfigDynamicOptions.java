package xaero.pac.common.player.config.dynamic;

import xaero.pac.common.server.player.config.PlayerConfigOptionSpec;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PlayerConfigDynamicOptions {

	private final Map<String, IPlayerConfigOptionSpecAPI<?>> options;

	private PlayerConfigDynamicOptions(Map<String, IPlayerConfigOptionSpecAPI<?>> options) {
		this.options = options;
	}

	public Map<String, IPlayerConfigOptionSpecAPI<?>> getOptions() {
		return options;
	}

	public static final class Builder {

		private Map<String, IPlayerConfigOptionSpecAPI<?>> options;

		private Builder(){
			options = new LinkedHashMap<>();
		}

		public Builder setDefault() {
			options.clear();
			return this;
		}

		public Builder addOption(PlayerConfigOptionSpec<?> option){
			if(!option.isDynamic())
				throw new IllegalArgumentException("tried to add a static option to dynamic options!");
			options.put(option.getId(), option);
			return this;
		}

		public PlayerConfigDynamicOptions build(){
			return new PlayerConfigDynamicOptions(Collections.unmodifiableMap(options));
		}

		public static Builder begin(){
			return new Builder().setDefault();
		}

	}

}
