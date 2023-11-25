package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.config.BaseConfiguration;
import dev.huskuraft.effortless.config.PatternConfiguration;
import dev.huskuraft.effortless.config.PreviewConfiguration;
import dev.huskuraft.effortless.config.TransformerConfiguration;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

public class BaseConfigurationSerializer extends TagSerializer<BaseConfiguration> {

    private static final String TAG_PREVIEW_CONFIG = "Previews";
    private static final String TAG_TRANSFORMER_CONFIG = "Transformers";
    private static final String TAG_PATTERN_CONFIG = "Patterns";

    @Override
    public BaseConfiguration read(TagElement tag) {
        return new BaseConfiguration(
                tag.getAsRecord().getElement(TAG_PREVIEW_CONFIG, PreviewConfigurationSerializer::new),
                tag.getAsRecord().getElement(TAG_TRANSFORMER_CONFIG, TransformerConfigurationSerializer::new),
                tag.getAsRecord().getElement(TAG_PATTERN_CONFIG, PatternConfigurationSerializer::new)
        );
    }

    @Override
    public void write(TagElement tag, BaseConfiguration config) {
        tag.getAsRecord().putElement(TAG_PREVIEW_CONFIG, config.getPreviewConfig(), PreviewConfigurationSerializer::new);
        tag.getAsRecord().putElement(TAG_TRANSFORMER_CONFIG, config.getTransformerConfig(), TransformerConfigurationSerializer::new);
        tag.getAsRecord().putElement(TAG_PATTERN_CONFIG, config.getPatternConfig(), PatternConfigurationSerializer::new);
    }

    public static class PreviewConfigurationSerializer extends TagSerializer<PreviewConfiguration> {

        public PreviewConfiguration read(TagElement tag) {
            return new PreviewConfiguration();
        }

        public void write(TagElement tag, PreviewConfiguration config) {

        }
    }

    public static class TransformerConfigurationSerializer extends TagSerializer<TransformerConfiguration> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public TransformerConfiguration read(TagElement tag) {
            return new TransformerConfiguration(
                    tag.getAsRecord().getList(TAG_ARRAYS, TransformerSerializer.ArrayTransformerSerializer::new),
                    tag.getAsRecord().getList(TAG_MIRRORS, TransformerSerializer.MirrorTransformerSerializer::new),
                    tag.getAsRecord().getList(TAG_RADIALS, TransformerSerializer.RadialTransformerSerializer::new),
                    tag.getAsRecord().getList(TAG_ITEM_RANDOMIZERS, TransformerSerializer.ItemRandomizerSerializer::new));
        }

        @Override
        public void write(TagElement tag, TransformerConfiguration config) {
            tag.getAsRecord().putList(TAG_ARRAYS, config.getArrays(), TransformerSerializer.ArrayTransformerSerializer::new);
            tag.getAsRecord().putList(TAG_MIRRORS, config.getMirrors(), TransformerSerializer.MirrorTransformerSerializer::new);
            tag.getAsRecord().putList(TAG_RADIALS, config.getRadials(), TransformerSerializer.RadialTransformerSerializer::new);
            tag.getAsRecord().putList(TAG_ITEM_RANDOMIZERS, config.getItemRandomizers(), TransformerSerializer.ItemRandomizerSerializer::new);
        }
    }


    public static class PatternConfigurationSerializer extends TagSerializer<PatternConfiguration> {

        private static final String TAG_PATTERNS = "Patterns";

        @Override
        public PatternConfiguration read(TagElement tag) {
            return new PatternConfiguration(
                    tag.getAsRecord().getList(TAG_PATTERNS, PatternSerializer::new)
            );
        }

        @Override
        public void write(TagElement tag, PatternConfiguration Config) {
            tag.getAsRecord().putList(TAG_PATTERNS, Config.getPatterns(), PatternSerializer::new);
        }
    }


}
