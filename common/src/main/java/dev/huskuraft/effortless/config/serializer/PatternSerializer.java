package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

public class PatternSerializer extends TagSerializer<Pattern> {

    private static final String TAG_NAME = "Name";
    private static final String TAG_TRANSFORMERS = "Transformers";

    @Override
    public Pattern read(TagElement tag) {
        return new Pattern(
                tag.getAsRecord().getText(TAG_NAME),
                tag.getAsRecord().getList(TAG_TRANSFORMERS, TransformerSerializer::new)
        );
    }

    @Override
    public void write(TagElement tag, Pattern pattern) {
        tag.getAsRecord().putText(TAG_NAME, pattern.name());
        tag.getAsRecord().putList(TAG_TRANSFORMERS, pattern.transformers(), TransformerSerializer::new);
    }


}