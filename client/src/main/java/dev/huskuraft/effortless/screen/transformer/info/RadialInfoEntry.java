package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RadialInfoEntry extends TransformerInfoEntry<Radial> {

    public RadialInfoEntry(Entrance entrance, EntryList entryList, Radial radial) {
        super(entrance, entryList, radial);
    }

    @Override
    protected List<Text> getBasicComponents(Radial transformer) {
        var position = Text.text("Position: %s", getPositionDescription(transformer.position())).withStyle(TextStyle.GRAY);
        var axis = Text.text("Slices: %s", getIntegerDescription(transformer.slice())).withStyle(TextStyle.GRAY);
        return Stream.of(position, axis).collect(Collectors.toList());
    }

    @Override
    public Text getDisplayName(Radial transformer) {
        return Text.text("Radial Transformer");
    }
}
