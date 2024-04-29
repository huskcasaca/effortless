package dev.huskuraft.effortless.building.config.universal;

import java.util.List;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;

import dev.huskuraft.effortless.api.config.ConfigSerializer;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.RootConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;

public class RootSettingsConfigSerializer implements ConfigSerializer<RootConfig> {

    private static final String KEY_RENDER = "render";

    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD = "showOtherPlayersBuild";
    private static final String KEY_SHOW_BLOCK_PREVIEW = "showBlockPreview";
    private static final String KEY_MAX_RENDER_BLOCKS = "maxRenderBlocks";
    private static final String KEY_MAX_RENDER_DISTANCE = "maxRenderDistance";

    private static final String KEY_PATTERNS = "patterns";
    private static final String KEY_TRANSFORMERS = "transformers";

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), () -> getDefault().renderConfig().showOtherPlayersBuild(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), () -> getDefault().renderConfig().showBlockPreview(), Boolean.class::isInstance);
//        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_BLOCKS), () -> getDefault().renderSettings().maxRenderBlocks(), RenderSettings.MIN_MAX_RENDER_BLOCKS, RenderSettings.MAX_MAX_RENDER_BLOCKS);
//        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), () -> getDefault().renderSettings().maxRenderDistance(), RenderSettings.MIN_MAX_RENDER_DISTANCE, RenderSettings.MAX_MAX_RENDER_DISTANCE);
        spec.defineList(KEY_PATTERNS, () -> getDefault().patternConfig().patterns().stream().map(PatternConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
        spec.defineList(KEY_TRANSFORMERS, () -> getDefault().transformerPresets().arrayTransformers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
        return spec;
    }

    @Override
    public RootConfig deserialize(Config config) {
        validate(config);
        return new RootConfig(
                new RenderConfig(
                        config.get(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD)),
                        config.get(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW)),
//                        config.get(List.of(KEY_RENDER, KEY_MAX_RENDER_BLOCKS)),
//                        config.get(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE))
                        0,
                        0
                ),
                new PatternConfig(
                        config.<List<Config>>get(KEY_PATTERNS).stream().map(PatternConfigSerializer.INSTANCE::deserialize).toList()
                ),
                new TransformerPresets(
                        config.<List<Config>>get(KEY_TRANSFORMERS).stream().map(TransformerConfigSerializer.INSTANCE::deserialize).toList()
                )

        );
    }

    @Override
    public Config serialize(RootConfig settings) {
        var config = CommentedConfig.inMemory();
        config.set(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), settings.renderConfig().showOtherPlayersBuild());
        config.set(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), settings.renderConfig().showBlockPreview());
//        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_BLOCKS), settings.renderSettings().maxRenderBlocks());
//        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), settings.renderSettings().maxRenderDistance());
        config.set(KEY_PATTERNS, settings.patternConfig().patterns().stream().map(PatternConfigSerializer.INSTANCE::serialize).toList());
        config.set(KEY_TRANSFORMERS, settings.transformerPresets().transformers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList());
        validate(config);
        return config;
    }

    @Override
    public RootConfig getDefault() {
        return new RootConfig(
                new RenderConfig(),
                new PatternConfig(
                        Pattern.getPatternPresets()),
                new TransformerPresets(
                        Transformer.getDefaultTransformers())
        );
    }

}
