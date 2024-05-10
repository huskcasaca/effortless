package dev.huskuraft.effortless.building.config.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;

public class PatternTagSerializer implements TagSerializer<Pattern> {

    private static final String TAG_ID = "id";
    private static final String TAG_ENABLED = "Enabled";
    private static final String TAG_TRANSFORMERS = "Transformers";

    @Override
    public Pattern decode(TagElement tag) {
        return new Pattern(
                tag.asRecord().getUUID(TAG_ID),
                tag.asRecord().getBoolean(TAG_ENABLED),
                tag.asRecord().getList(TAG_TRANSFORMERS, new TransformerTagSerializer())
        );
    }

    @Override
    public TagElement encode(Pattern pattern) {
        var tag = TagRecord.newRecord();
        tag.asRecord().putUUID(TAG_ID, pattern.id());
        tag.asRecord().putBoolean(TAG_ENABLED, pattern.enabled());
        tag.asRecord().putList(TAG_TRANSFORMERS, pattern.transformers(), new TransformerTagSerializer());
        return tag;
    }


}
