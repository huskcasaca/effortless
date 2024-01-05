package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;

public class PatternSerializer implements TagSerializer<Pattern> {

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_TRANSFORMERS = "Transformers";

    @Override
    public Pattern read(TagElement tag) {
        return new Pattern(
                tag.asRecord().getUUID(TAG_ID),
                tag.asRecord().getText(TAG_NAME),
                tag.asRecord().getList(TAG_TRANSFORMERS, new TransformerSerializer())
        );
    }

    @Override
    public void write(TagElement tag, Pattern pattern) {
        tag.asRecord().putUUID(TAG_ID, pattern.id());
        tag.asRecord().putText(TAG_NAME, pattern.name());
        tag.asRecord().putList(TAG_TRANSFORMERS, pattern.transformers(), new TransformerSerializer());
    }


}