package dev.huskuraft.effortless.building.config.universal;

import java.util.List;
import java.util.Objects;

import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.ClipboardConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.universal.api.config.ConfigSerializer;
import dev.huskuraft.universal.api.nightconfig.core.CommentedConfig;
import dev.huskuraft.universal.api.nightconfig.core.Config;
import dev.huskuraft.universal.api.nightconfig.core.ConfigSpec;

public class ClientConfigConfigSerializer implements ConfigSerializer<ClientConfig> {

    private static final String KEY_RENDER = "render";
    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD = "showOtherPlayersBuild";
    //    private static final String KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS = "showOtherPlayersBuildTooltips";
    private static final String KEY_SHOW_BLOCK_PREVIEW = "showBlockPreview";
    private static final String KEY_MAX_RENDER_VOLUME = "maxRenderVolume";

    private static final String KEY_BUILDER = "builder";
    private static final String KEY_PATTERN = "pattern";
    private static final String KEY_TRANSFORMER_PRESETS = "transformerPresets";
    private static final String KEY_CLIPBOARD = "clipboard";
    private static final String KEY_COLLECTIONS = "collections";

    private static final String KEY_RESERVED_TOOL_DURABILITY = "reservedToolDurability";


    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();

        spec.define(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), () -> getDefault().renderConfig().showBlockPreview(), Boolean.class::isInstance);
        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), () -> getDefault().renderConfig().showOtherPlayersBuild(), Boolean.class::isInstance);
//        spec.define(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS), () -> getDefault().renderConfig().showOtherPlayersBuildTooltips(), Boolean.class::isInstance);
        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), getDefault().renderConfig().maxRenderVolume(), RenderConfig.MAX_RENDER_VOLUME_MIN, RenderConfig.MAX_RENDER_VOLUME_MAX);
//        spec.defineInRange(List.of(KEY_RENDER, KEY_MAX_RENDER_DISTANCE), () -> getDefault().renderConfig().maxRenderDistance(), RenderConfig.MIN_MAX_RENDER_DISTANCE, RenderConfig.MAX_MAX_RENDER_DISTANCE);
        spec.defineList(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS), () -> getDefault().patternConfig().itemRandomizers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
        spec.defineList(List.of(KEY_CLIPBOARD, KEY_COLLECTIONS), () -> getDefault().clipboardConfig().collections().stream().map(SnapshotConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
//        spec.define(KEY_PASSIVE_MODE, () -> getDefault().passiveMode(), Boolean.class::isInstance);
        spec.defineInRange(List.of(KEY_BUILDER, KEY_RESERVED_TOOL_DURABILITY), getDefault().builderConfig().reservedToolDurability(), 0, 32);

        return spec;
    }

    @Override
    public ClientConfig deserialize(Config config) {
        validate(config);
        return new ClientConfig(
                new RenderConfig(
                        config.get(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW)),
                        config.get(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD)),
                        false,
//                        config.get(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS)),
                        config.get(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME)),
                        128
                ),
                new PatternConfig(
                        config.<List<Config>>get(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS)).stream().map(TransformerConfigSerializer.INSTANCE::deserialize).toList()
                ),
                new ClipboardConfig(
                        config.<List<Config>>get(List.of(KEY_CLIPBOARD, KEY_COLLECTIONS)).stream().map(SnapshotConfigSerializer.INSTANCE::deserialize).toList(),
                        List.of()
                )
        );
    }

    @Override
    public Config serialize(ClientConfig settings) {
        var config = CommentedConfig.inMemory();
        config.set(List.of(KEY_RENDER, KEY_SHOW_BLOCK_PREVIEW), settings.renderConfig().showBlockPreview());
        config.set(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD), settings.renderConfig().showOtherPlayersBuild());
//        config.set(List.of(KEY_RENDER, KEY_SHOW_OTHER_PLAYERS_BUILD_TOOLTIPS), settings.renderConfig().showOtherPlayersBuildTooltips());
        config.set(List.of(KEY_RENDER, KEY_MAX_RENDER_VOLUME), settings.renderConfig().maxRenderVolume());
        config.set(List.of(KEY_PATTERN, KEY_TRANSFORMER_PRESETS), settings.patternConfig().itemRandomizers().stream().map(TransformerConfigSerializer.INSTANCE::serialize).filter(Objects::nonNull).toList());
        config.set(List.of(KEY_CLIPBOARD, KEY_COLLECTIONS), settings.clipboardConfig().collections().stream().map(SnapshotConfigSerializer.INSTANCE::serialize).filter(Objects::nonNull).toList());
        validate(config);
        return config;
    }

    @Override
    public ClientConfig getDefault() {
        return ClientConfig.DEFAULT;
    }

}
