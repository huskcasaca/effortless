package xaero.pac.common.server.player.config.io.serialization;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.io.ParsingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import xaero.pac.common.misc.ConfigUtil;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.config.sub.PlayerSubConfig;

import java.util.LinkedHashMap;
import java.util.Objects;

public class PlayerConfigSerializer {

	private TomlParser parser;
	private TomlWriter writer;

	public PlayerConfigSerializer() {
		this.parser = new TomlParser();
		this.writer = new TomlWriter();
	}

	public String serialize(PlayerConfig<?> config) {
		return writer.writeToString(config.getStorage());
	}

	public void deserializeInto(PlayerConfig<?> config, String serializedData) {
		CommentedConfig parsedData = CommentedConfig.of(LinkedHashMap::new, TomlFormat.instance());
		parser.parse(serializedData, parsedData, ParsingMode.ADD);
		if(!(config instanceof PlayerSubConfig))
			config.getManager().getPlayerConfigSpec().correct(parsedData);
		Config loadedConfig;
		if(config.getPlayerId() != null && !Objects.equals(config.getPlayerId(), PlayerConfig.SERVER_CLAIM_UUID) && !Objects.equals(config.getPlayerId(), PlayerConfig.EXPIRED_CLAIM_UUID)) {
			loadedConfig = ConfigUtil.deepCopy(parsedData, LinkedHashMap::new);//removes comments
		} else
			loadedConfig = parsedData;
		config.setStorage(loadedConfig);

		//fixing incorrect value types
		config.getManager().getAllOptionsStream().forEach(o -> {
			Object rawOptionValue = loadedConfig.get(o.getPath());
			if(rawOptionValue != null && rawOptionValue.getClass() != o.getType()) {
				Object defaultRawValue = config.getDefaultRawValue(o);
				if(defaultRawValue == null)
					loadedConfig.remove(o.getPath());
				else
					loadedConfig.set(o.getPath(), defaultRawValue);
			}
		});
	}

}
