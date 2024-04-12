package dev.huskuraft.effortless.building.config.universal;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;

import dev.huskuraft.effortless.api.config.ConfigSerializer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Pattern;

public class PatternConfigSerializer implements ConfigSerializer<Pattern> {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TRANSFORMERS = "transformers";

    private PatternConfigSerializer() {
    }

    public static final PatternConfigSerializer INSTANCE = new PatternConfigSerializer();

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(KEY_ID, PatternConfigSerializer::randomIdString, PatternConfigSerializer::isIdCorrect);
        spec.define(KEY_NAME, getDefault().name().getString(), String.class::isInstance);
        spec.defineList(KEY_TRANSFORMERS, getDefault().transformers(), Config.class::isInstance);
        return spec;
    }

    @Override
    public Pattern deserialize(Config config) {
        validate(config);
        return new Pattern(
                UUID.fromString(config.get(KEY_ID)),
                Text.text(config.get(KEY_NAME)),
                config.<List<Config>>get(KEY_TRANSFORMERS).stream().map(TransformerConfigSerializer.INSTANCE::deserialize).filter(Objects::nonNull).toList()
        );
    }

    @Override
    public Config serialize(Pattern pattern) {
        var config = Config.inMemory();
        config.set(KEY_ID, pattern.id().toString());
        config.set(KEY_NAME, pattern.name().getString());
        config.set(KEY_TRANSFORMERS, pattern.transformers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList());
        validate(config);
        return config;
    }

    @Override
    public Pattern getDefault() {
        return Pattern.getDefaultPattern();
    }

    public static String randomIdString() {
        return UUID.randomUUID().toString();
    }

    public static boolean isIdCorrect(Object string) {
        try {
            UUID.fromString((String) string);
            return true;
        } catch (ClassCastException | IllegalArgumentException e) {
            return false;
        }
    }


}
