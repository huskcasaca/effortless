package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.config.BaseConfiguration;
import dev.huskuraft.effortless.config.PatternConfiguration;
import dev.huskuraft.effortless.config.PreviewConfiguration;
import dev.huskuraft.effortless.config.TransformerConfiguration;

public class BaseConfigurationSerializer implements TagSerializer<BaseConfiguration> {

    private static final String TAG_PREVIEW_CONFIG = "Previews";
    private static final String TAG_TRANSFORMER_CONFIG = "Transformers";
    private static final String TAG_PATTERN_CONFIG = "Patterns";

    @Override
    public BaseConfiguration read(TagElement tag) {
        return new BaseConfiguration(
                tag.asRecord().getElement(TAG_PREVIEW_CONFIG, new PreviewConfigurationSerializer()),
                tag.asRecord().getElement(TAG_TRANSFORMER_CONFIG, new TransformerConfigurationSerializer()),
                tag.asRecord().getElement(TAG_PATTERN_CONFIG, new PatternConfigurationSerializer())
        );
    }

    @Override
    public void write(TagElement tag, BaseConfiguration config) {
        tag.asRecord().putElement(TAG_PREVIEW_CONFIG, config.getPreviewConfig(), new PreviewConfigurationSerializer());
        tag.asRecord().putElement(TAG_TRANSFORMER_CONFIG, config.getTransformerConfig(), new TransformerConfigurationSerializer());
        tag.asRecord().putElement(TAG_PATTERN_CONFIG, config.getPatternConfig(), new PatternConfigurationSerializer());
    }

    public static class PreviewConfigurationSerializer implements TagSerializer<PreviewConfiguration> {

        public PreviewConfiguration read(TagElement tag) {
            return new PreviewConfiguration();
        }

        public void write(TagElement tag, PreviewConfiguration config) {
            tag.asRecord(); // avoid NPE
        }
    }

    public static class TransformerConfigurationSerializer implements TagSerializer<TransformerConfiguration> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public TransformerConfiguration read(TagElement tag) {
            return new TransformerConfiguration(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerSerializer.ArrayTransformerSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerSerializer.MirrorTransformerSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerSerializer.RadialTransformerSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerSerializer.ItemRandomizerSerializer()));
        }

        @Override
        public void write(TagElement tag, TransformerConfiguration config) {
            tag.asRecord().putList(TAG_ARRAYS, config.getArrays(), new TransformerSerializer.ArrayTransformerSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.getMirrors(), new TransformerSerializer.MirrorTransformerSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.getRadials(), new TransformerSerializer.RadialTransformerSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.getItemRandomizers(), new TransformerSerializer.ItemRandomizerSerializer());
        }
    }


    public static class PatternConfigurationSerializer implements TagSerializer<PatternConfiguration> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternConfiguration read(TagElement tag) {
            return new PatternConfiguration(
                    tag.asRecord().getList(TAG_PATTERNS, new PatternSerializer())
            );
        }

        @Override
        public void write(TagElement tag, PatternConfiguration Config) {
            tag.asRecord().putList(TAG_PATTERNS, Config.getPatterns(), new PatternSerializer());
        }
    }


}
