package dev.huskuraft.effortless.building.config.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;

public class ClientConfigTagSerializer implements TagSerializer<ClientConfig> {

    private static final String TAG_RENDER_CONFIG = "RenderConfig";
    private static final String TAG_PATTERN_CONFIG = "PatternConfig";

    @Override
    public ClientConfig decode(TagElement tag) {
        return new ClientConfig(
                tag.asRecord().getTag(TAG_RENDER_CONFIG, new RenderSettingsTagSerializer()),
                tag.asRecord().getTag(TAG_PATTERN_CONFIG, new PatternConfigTagSerializer())
        );
    }

    @Override
    public TagElement encode(ClientConfig config) {
        var tag = TagRecord.newRecord();
        tag.asRecord().putTag(TAG_RENDER_CONFIG, config.renderConfig(), new RenderSettingsTagSerializer());
        tag.asRecord().putTag(TAG_PATTERN_CONFIG, config.patternConfig(), new PatternConfigTagSerializer());
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

    public static class PatternConfigTagSerializer implements TagSerializer<PatternConfig> {

        private static final String TAG_ARRAYS = "Arrays";
        private static final String TAG_MIRRORS = "Mirrors";
        private static final String TAG_RADIALS = "Radials";
        private static final String TAG_ITEM_RANDOMIZERS = "ItemRandomizers";

        @Override
        public PatternConfig decode(TagElement tag) {
            return new PatternConfig(
                    tag.asRecord().getList(TAG_ARRAYS, new TransformerTagSerializer.ArrayTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_MIRRORS, new TransformerTagSerializer.MirrorTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_RADIALS, new TransformerTagSerializer.RadialTransformerTagSerializer()),
                    tag.asRecord().getList(TAG_ITEM_RANDOMIZERS, new TransformerTagSerializer.ItemRandomizerTagSerializer()));
        }

        @Override
        public TagElement encode(PatternConfig config) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putList(TAG_ARRAYS, config.arrayTransformers(), new TransformerTagSerializer.ArrayTransformerTagSerializer());
            tag.asRecord().putList(TAG_MIRRORS, config.mirrorTransformers(), new TransformerTagSerializer.MirrorTransformerTagSerializer());
            tag.asRecord().putList(TAG_RADIALS, config.radialTransformers(), new TransformerTagSerializer.RadialTransformerTagSerializer());
            tag.asRecord().putList(TAG_ITEM_RANDOMIZERS, config.itemRandomizers(), new TransformerTagSerializer.ItemRandomizerTagSerializer());
            return tag;
        }
    }




}
