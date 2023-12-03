package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayTransformerInfoEntry extends TransformerInfoEntry<ArrayTransformer> {

    public ArrayTransformerInfoEntry(Entrance entrance, EntryList entryList, ArrayTransformer arrayTransformer) {
        super(entrance, entryList, arrayTransformer);
    }

    @Override
    protected List<Text> getBasicComponents(ArrayTransformer transformer) {
        var position = Text.translate("effortless.transformer.array.offset", getPositionDescription(transformer.offset())).withStyle(TextStyle.GRAY);
        var repeats = Text.translate("effortless.transformer.array.count", getIntegerDescription(transformer.count())).withStyle(TextStyle.GRAY);
        return Stream.of(position, repeats).collect(Collectors.toList());
    }

    @Override
    protected Text getDisplayName(ArrayTransformer transformer) {
        return Text.text("Array Transformer");
    }
}
