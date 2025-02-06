package xaero.pac.common.misc;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;

import java.util.Map;
import java.util.function.Supplier;

public class ConfigUtil {

	public static Config deepCopy(UnmodifiableConfig toCopy, Supplier<Map<String,Object>> mapCreator) {
		Config copy = Config.of(mapCreator, toCopy.configFormat());
		toCopy.valueMap().forEach((k,v) -> {
			Object valueCopy = v;
			if(v instanceof UnmodifiableConfig)
				valueCopy = deepCopy((UnmodifiableConfig) v, mapCreator);
			copy.set(k, valueCopy);
		});
		return copy;
	}

}
