package dev.huskuraft.effortless.screen.transformer.tooltip;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRandomizerTooltipEntry extends TransformerTooltipEntry<ItemRandomizer> {

    public ItemRandomizerTooltipEntry(Entrance entrance, EntryList entryList, ItemRandomizer randomizer) {
        super(entrance, entryList, randomizer);
    }

    @Override
    protected List<SlotData> getInfo() {
        return getItem().getChances().stream().map(chance -> new SlotData.ItemStackSymbol(chance.content().getDefaultStack(), Text.text(String.valueOf(chance.chance())))).collect(Collectors.toList());
    }

}
