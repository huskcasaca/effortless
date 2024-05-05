package dev.huskuraft.effortless.building.config.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;

public class RootSettingsTagSerializer implements TagSerializer<ClientConfig> {

    private static final String TAG_RENDER_SETTINGS = "Render";
    private static final String TAG_PATTERN_SETTINGS = "Patterns";
    private static final String TAG_TRANSFORMER_PRESETS = "Transformers";
    private static final String TAG_PASSIVE_MODE = "PassiveMode";

    @Override
    public ClientConfig decode(TagElement tag) {
        return new ClientConfig(
                tag.asRecord().getTag(TAG_RENDER_SETTINGS, new RenderSettingsTagSerializer()),
                tag.asRecord().getTag(TAG_PATTERN_SETTINGS, new PatternSettingsTagSerializer()),
                tag.asRecord().getTag(TAG_TRANSFORMER_PRESETS, new TransformerPresetsTagSerializer()),
                tag.asRecord().getBoolean(TAG_PASSIVE_MODE)
        );
    }

    @Override
    public TagElement encode(ClientConfig config) {
        var tag = TagRecord.newRecord();
        tag.asRecord().putTag(TAG_RENDER_SETTINGS, config.renderConfig(), new RenderSettingsTagSerializer());
        tag.asRecord().putTag(TAG_PATTERN_SETTINGS, config.patternConfig(), new PatternSettingsTagSerializer());
        tag.asRecord().putTag(TAG_TRANSFORMER_PRESETS, config.transformerPresets(), new TransformerPresetsTagSerializer());
        tag.asRecord().putBoolean(TAG_PASSIVE_MODE, config.passiveMode());
        return tag;
    }

    public static class RenderSettingsTagSerializer implements TagSerializer<RenderConfig> {

        public RenderConfig decode(TagElement tag) {
            return new RenderConfig();
        }

        public TagElement encode(RenderConfig config) {
            return TagRecord.newRecord(); // avoid NPE
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
        public TransformerPresets decode(TagElement tag) {
            return new TransformerPresets(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerTagSerializer.ArrayTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerTagSerializer.MirrorTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerTagSerializer.RadialTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerTagSerializer.ItemRandomizerTagSerializer()));
        }

        @Override
        public TagElement encode(TransformerPresets config) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putList(TAG_ARRAYS, config.arrayTransformers(), new TransformerTagSerializer.ArrayTransformerTagSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.mirrorTransformers(), new TransformerTagSerializer.MirrorTransformerTagSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.radialTransformers(), new TransformerTagSerializer.RadialTransformerTagSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.itemRandomizers(), new TransformerTagSerializer.ItemRandomizerTagSerializer());
            return tag;
        }
    }


    public static class PatternSettingsTagSerializer implements TagSerializer<PatternConfig> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternConfig decode(TagElement tag) {
            return new PatternConfig(
                    tag.asRecord().getList(TAG_PATTERNS, new PatternTagSerializer())
            );
        }

        @Override
        public TagElement encode(PatternConfig Config) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putList(TAG_PATTERNS, Config.patterns(), new PatternTagSerializer());
            return tag;
        }
    }


}
