package dev.huskuraft.effortless.building.config.universal;

import java.util.List;
import java.util.Objects;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;

import dev.huskuraft.effortless.api.config.ConfigSerializer;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;

public class ClientConfigConfigSerializer implements ConfigSerializer<ClientConfig> {

    private static final String KEY_RENDER = "render";
    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD = "showOtherPlayersBuild";
    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS = "showOtherPlayersBuildTooltips";
    private static final String KEY_SHOW_BLOCK_PREVIEW = "showBlockPreview";
    private static final String KEY_MAX_RENDER_VOLUME = "maxRenderVolume";

    private static final String KEY_PATTERN = "pattern";
    private static final String KEY_TRANSFORMER_PRESETS = "transformerPresets";

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), () -> getDefault().renderConfig().showOtherPlayersBuild(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS), () -> getDefault().renderConfig().showOtherPlayersBuildTooltips(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), () -> getDefault().renderConfig().showBlockPreview(), Boolean.class::isInstance);
        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), getDefault().renderConfig().maxRenderVolume(), RenderConfig.MAX_RENDER_VOLUME_MIN, RenderConfig.MAX_RENDER_VOLUME_MAX);
//        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), () -> getDefault().renderConfig().maxRenderDistance(), RenderConfig.MIN_MAX_RENDER_DISTANCE, RenderConfig.MAX_MAX_RENDER_DISTANCE);
        spec.defineList(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS), () -> getDefault().patternConfig().itemRandomizers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
//        spec.define(KEY_PASSIVE_MODE, () -> getDefault().passiveMode(), Boolean.class::isInstance);

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
                        config.<List<Config>>get(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS)).stream().map(TransformerConfigSerializer.INSTANCE::deserialize).toList()
                )
        );
    }

    @Override
    public Config serialize(ClientConfig settings) {
        var config = CommentedConfig.inMemory();
        config.set(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), settings.renderConfig().showOtherPlayersBuild());
        config.set(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), settings.renderConfig().showBlockPreview());
        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), settings.renderConfig().maxRenderVolume());
//        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), settings.renderConfig().maxRenderDistance());
//        config.set(KEY_PATTERNS, settings.patternConfig().patterns().stream().map(PatternConfigSerializer.INSTANCE::serialize).toList());
        config.set(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS), settings.patternConfig().itemRandomizers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).filter(Objects::nonNull).toList());
        validate(config);
        return config;
    }

    @Override
    public ClientConfig getDefault() {
        return ClientConfig.DEFAULT;
    }

}
