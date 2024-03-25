package dev.huskuraft.effortless.building.config.serializer;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.PatternSettings;
import dev.huskuraft.effortless.building.config.PreviewSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.config.TransformerPresets;

public class RootSettingsTagSerializer implements TagSerializer<RootSettings> {

    private static final String TAG_PREVIEW_SETTINGS = "Previews";
    private static final String TAG_PATTERN_SETTINGS = "Patterns";
    private static final String TAG_TRANSFORMER_PRESETS = "Transformers";

    @Override
    public RootSettings read(TagElement tag) {
        return new RootSettings(
                tag.asRecord().getElement(TAG_PREVIEW_SETTINGS, new PreviewConfigurationSerializer()),
                tag.asRecord().getElement(TAG_PATTERN_SETTINGS, new PatternConfigurationSerializer()),
                tag.asRecord().getElement(TAG_TRANSFORMER_PRESETS, new TransformerConfigurationSerializer())
        );
    }

    @Override
    public void write(TagElement tag, RootSettings config) {
        tag.asRecord().putElement(TAG_PREVIEW_SETTINGS, config.previewSettings(), new PreviewConfigurationSerializer());
        tag.asRecord().putElement(TAG_PATTERN_SETTINGS, config.patternSettings(), new PatternConfigurationSerializer());
        tag.asRecord().putElement(TAG_TRANSFORMER_PRESETS, config.transformerPresets(), new TransformerConfigurationSerializer());
    }

    public static class PreviewConfigurationSerializer implements TagSerializer<PreviewSettings> {

        public PreviewSettings read(TagElement tag) {
            return new PreviewSettings();
        }

        public void write(TagElement tag, PreviewSettings config) {
            tag.asRecord(); // avoid NPE
        }
    }

    public static class TransformerConfigurationSerializer implements TagSerializer<TransformerPresets> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public TransformerPresets read(TagElement tag) {
            return new TransformerPresets(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerTagSerializer.ArrayTransformerSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerTagSerializer.MirrorTransformerSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerTagSerializer.RadialTransformerSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerTagSerializer.ItemRandomizerSerializer()));
        }

        @Override
        public void write(TagElement tag, TransformerPresets config) {
            tag.asRecord().putList(TAG_ARRAYS, config.arrayTransformers(), new TransformerTagSerializer.ArrayTransformerSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.mirrorTransformers(), new TransformerTagSerializer.MirrorTransformerSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.radialTransformers(), new TransformerTagSerializer.RadialTransformerSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.itemRandomizers(), new TransformerTagSerializer.ItemRandomizerSerializer());
        }
    }


    public static class PatternConfigurationSerializer implements TagSerializer<PatternSettings> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternSettings read(TagElement tag) {
            return new PatternSettings(
                    tag.asRecord().getList(TAG_PATTERNS, new PatternTagSerializer())
            );
        }

        @Override
        public void write(TagElement tag, PatternSettings Config) {
            tag.asRecord().putList(TAG_PATTERNS, Config.patterns(), new PatternTagSerializer());
        }
    }


}
