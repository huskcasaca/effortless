package dev.huskuraft.effortless.building.config.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.RootConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;

public class RootSettingsTagSerializer implements TagSerializer<RootConfig> {

    private static final String TAG_RENDER_SETTINGS = "Render";
    private static final String TAG_PATTERN_SETTINGS = "Patterns";
    private static final String TAG_TRANSFORMER_PRESETS = "Transformers";

    @Override
    public RootConfig read(TagElement tag) {
        return new RootConfig(
                tag.asRecord().getElement(TAG_RENDER_SETTINGS, new RenderSettingsTagSerializer()),
                tag.asRecord().getElement(TAG_PATTERN_SETTINGS, new PatternSettingsTagSerializer()),
                tag.asRecord().getElement(TAG_TRANSFORMER_PRESETS, new TransformerPresetsTagSerializer())
        );
    }

    @Override
    public void write(TagElement tag, RootConfig config) {
        tag.asRecord().putElement(TAG_RENDER_SETTINGS, config.renderConfig(), new RenderSettingsTagSerializer());
        tag.asRecord().putElement(TAG_PATTERN_SETTINGS, config.patternConfig(), new PatternSettingsTagSerializer());
        tag.asRecord().putElement(TAG_TRANSFORMER_PRESETS, config.transformerPresets(), new TransformerPresetsTagSerializer());
    }

    public static class RenderSettingsTagSerializer implements TagSerializer<RenderConfig> {

        public RenderConfig read(TagElement tag) {
            return new RenderConfig();
        }

        public void write(TagElement tag, RenderConfig config) {
            tag.asRecord(); // avoid NPE
        }

        public RenderConfig getFallback() {
            return new RenderConfig();
        }
    }

    public static class TransformerPresetsTagSerializer implements TagSerializer<TransformerPresets> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public TransformerPresets read(TagElement tag) {
            return new TransformerPresets(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerTagSerializer.ArrayTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerTagSerializer.MirrorTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerTagSerializer.RadialTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerTagSerializer.ItemRandomizerTagSerializer()));
        }

        @Override
        public void write(TagElement tag, TransformerPresets config) {
            tag.asRecord().putList(TAG_ARRAYS, config.arrayTransformers(), new TransformerTagSerializer.ArrayTransformerTagSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.mirrorTransformers(), new TransformerTagSerializer.MirrorTransformerTagSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.radialTransformers(), new TransformerTagSerializer.RadialTransformerTagSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.itemRandomizers(), new TransformerTagSerializer.ItemRandomizerTagSerializer());
        }
    }


    public static class PatternSettingsTagSerializer implements TagSerializer<PatternConfig> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternConfig read(TagElement tag) {
            return new PatternConfig(
                    tag.asRecord().getList(TAG_PATTERNS, new PatternTagSerializer())
            );
        }

        @Override
        public void write(TagElement tag, PatternConfig Config) {
            tag.asRecord().putList(TAG_PATTERNS, Config.patterns(), new PatternTagSerializer());
        }
    }


}
