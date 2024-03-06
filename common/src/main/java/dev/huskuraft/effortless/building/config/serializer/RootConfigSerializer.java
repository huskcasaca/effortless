package dev.huskuraft.effortless.building.config.serializer;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.PreviewConfig;
import dev.huskuraft.effortless.building.config.RootConfig;
import dev.huskuraft.effortless.building.config.TransformerConfig;

public class RootConfigSerializer implements TagSerializer<RootConfig> {

    private static final String TAG_PREVIEW_CONFIG = "Previews";
    private static final String TAG_TRANSFORMER_CONFIG = "Transformers";
    private static final String TAG_PATTERN_CONFIG = "Patterns";

    @Override
    public RootConfig read(TagElement tag) {
        return new RootConfig(
                tag.asRecord().getElement(TAG_PREVIEW_CONFIG, new PreviewConfigurationSerializer()),
                tag.asRecord().getElement(TAG_TRANSFORMER_CONFIG, new TransformerConfigurationSerializer()),
                tag.asRecord().getElement(TAG_PATTERN_CONFIG, new PatternConfigurationSerializer())
        );
    }

    @Override
    public void write(TagElement tag, RootConfig config) {
        tag.asRecord().putElement(TAG_PREVIEW_CONFIG, config.getPreviewConfig(), new PreviewConfigurationSerializer());
        tag.asRecord().putElement(TAG_TRANSFORMER_CONFIG, config.getTransformerConfig(), new TransformerConfigurationSerializer());
        tag.asRecord().putElement(TAG_PATTERN_CONFIG, config.getPatternConfig(), new PatternConfigurationSerializer());
    }

    public static class PreviewConfigurationSerializer implements TagSerializer<PreviewConfig> {

        public PreviewConfig read(TagElement tag) {
            return new PreviewConfig();
        }

        public void write(TagElement tag, PreviewConfig config) {
            tag.asRecord(); // avoid NPE
        }
    }

    public static class TransformerConfigurationSerializer implements TagSerializer<TransformerConfig> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public TransformerConfig read(TagElement tag) {
            return new TransformerConfig(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerSerializer.ArrayTransformerSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerSerializer.MirrorTransformerSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerSerializer.RadialTransformerSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerSerializer.ItemRandomizerSerializer()));
        }

        @Override
        public void write(TagElement tag, TransformerConfig config) {
            tag.asRecord().putList(TAG_ARRAYS, config.getArrays(), new TransformerSerializer.ArrayTransformerSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.getMirrors(), new TransformerSerializer.MirrorTransformerSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.getRadials(), new TransformerSerializer.RadialTransformerSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.getItemRandomizers(), new TransformerSerializer.ItemRandomizerSerializer());
        }
    }


    public static class PatternConfigurationSerializer implements TagSerializer<PatternConfig> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternConfig read(TagElement tag) {
            return new PatternConfig(
                    tag.asRecord().getList(TAG_PATTERNS, new PatternSerializer())
            );
        }

        @Override
        public void write(TagElement tag, PatternConfig Config) {
            tag.asRecord().putList(TAG_PATTERNS, Config.getPatterns(), new PatternSerializer());
        }
    }


}
