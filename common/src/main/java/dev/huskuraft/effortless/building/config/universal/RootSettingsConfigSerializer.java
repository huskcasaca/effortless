package dev.huskuraft.effortless.building.config.universal;

import java.util.List;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;

import dev.huskuraft.effortless.api.config.ConfigSerializer;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;

public class RootSettingsConfigSerializer implements ConfigSerializer<ClientConfig> {

    private static final String KEY_RENDER = "render";

    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD = "showOtherPlayersBuild";
    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS = "showOtherPlayersBuildTooltips";
    private static final String KEY_SHOW_BLOCK_PREVIEW = "showBlockPreview";
    private static final String KEY_MAX_RENDER_VOLUME = "maxRenderVolume";
    private static final String KEY_PASSIVE_MODE = "passiveMode";

    private static final String KEY_PATTERNS = "patterns";
    private static final String KEY_TRANSFORMERS = "transformers";

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), () -> getDefault().renderConfig().showOtherPlayersBuild(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS), () -> getDefault().renderConfig().showOtherPlayersBuildTooltips(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), () -> getDefault().renderConfig().showBlockPreview(), Boolean.class::isInstance);
        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), getDefault().renderConfig().maxRenderVolume(), RenderConfig.MAX_RENDER_VOLUME_MIN, RenderConfig.MAX_RENDER_VOLUME_MAX);
//        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), () -> getDefault().renderConfig().maxRenderDistance(), RenderConfig.MIN_MAX_RENDER_DISTANCE, RenderConfig.MAX_MAX_RENDER_DISTANCE);
        spec.defineList(KEY_PATTERNS, () -> getDefault().patternConfig().patterns().stream().map(PatternConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
        spec.defineList(KEY_TRANSFORMERS, () -> getDefault().transformerPresets().arrayTransformers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
        spec.define(KEY_PASSIVE_MODE, () -> getDefault().passiveMode(), Boolean.class::isInstance);

        return spec;
    }

    @Override
    public ClientConfig deserialize(Config config) {
        validate(config);
        return new ClientConfig(
                new RenderConfig(
                        config.get(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD)),
                        config.get(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS)),
                        config.get(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW)),
                        config.get(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME)),
//                        config.get(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE))
                        128
                ),
                new PatternConfig(
                        config.<List<Config>>get(KEY_PATTERNS).stream().map(PatternConfigSerializer.INSTANCE::deserialize).toList()
                ),
                new TransformerPresets(
                        config.<List<Config>>get(KEY_TRANSFORMERS).stream().map(TransformerConfigSerializer.INSTANCE::deserialize).toList()
                ),
                config.get(List.of(KEY_PASSIVE_MODE))

        );
    }

    @Override
    public Config serialize(ClientConfig settings) {
        var config = CommentedConfig.inMemory();
        config.set(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), settings.renderConfig().showOtherPlayersBuild());
        config.set(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), settings.renderConfig().showBlockPreview());
        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), settings.renderConfig().maxRenderVolume());
//        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), settings.renderConfig().maxRenderDistance());
        config.set(KEY_PATTERNS, settings.patternConfig().patterns().stream().map(PatternConfigSerializer.INSTANCE::serialize).toList());
        config.set(KEY_TRANSFORMERS, settings.transformerPresets().transformers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList());
        validate(config);
        return config;
    }

    @Override
    public ClientConfig getDefault() {
        return ClientConfig.getDefault();
    }

}
