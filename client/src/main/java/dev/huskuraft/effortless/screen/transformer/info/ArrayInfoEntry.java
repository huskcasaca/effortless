package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayInfoEntry extends TransformerInfoEntry<Array> {

    public ArrayInfoEntry(Entrance entrance, EntryList entryList, Array array) {
        super(entrance, entryList, array);
    }

    @Override
    protected List<Text> getBasicComponents(Array transformer) {
        var position = Text.translate("transformer.array.offset", getPositionDescription(transformer.offset())).withStyle(TextStyle.GRAY);
        var repeats = Text.translate("transformer.array.count", getIntegerDescription(transformer.count())).withStyle(TextStyle.GRAY);
        return Stream.of(position, repeats).collect(Collectors.toList());
    }

    @Override
    protected Text getDisplayName(Array transformer) {
        return Text.text("Array Transformer");
    }
}
