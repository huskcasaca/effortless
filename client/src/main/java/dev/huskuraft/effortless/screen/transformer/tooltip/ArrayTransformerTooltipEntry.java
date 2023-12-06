package dev.huskuraft.effortless.screen.transformer.tooltip;

import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.text.Text;

import java.util.List;

public class ArrayTransformerTooltipEntry extends TransformerTooltipEntry<ArrayTransformer> {

    public ArrayTransformerTooltipEntry(Entrance entrance, EntryList entryList, ArrayTransformer arrayTransformer) {
        super(entrance, entryList, arrayTransformer);
    }

    @Override
    protected List<SlotData> getInfo() {
        return List.of(
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().x())), Text.translate("effortless.axis.x")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().y())), Text.translate("effortless.axis.y")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().z())), Text.translate("effortless.axis.z")),
                new SlotData.TextSymbol(Text.text(formatDouble(getItem().count())), Text.translate("effortless.transformer.array.count"))
        );
    }

}
