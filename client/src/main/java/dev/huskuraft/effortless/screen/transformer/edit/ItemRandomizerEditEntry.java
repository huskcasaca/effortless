package dev.huskuraft.effortless.screen.transformer.edit;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;

public class ItemRandomizerEditEntry extends TransformerEditEntry<ItemRandomizer> {

    public ItemRandomizerEditEntry(Entrance entrance, EntryList entryList, ItemRandomizer transformer) {
        super(entrance, entryList, transformer);
    }

    private Text getCategoryDescription(Randomizer.Category category) {
        return Text.translate(category.getNameKey());
    }

    private Text getOrderDescription(Randomizer.Order order) {
        return Text.translate(order.getNameKey());
    }

    private Text getGroupDescription(Randomizer.Target target) {
        return Text.translate(target.getNameKey());
    }
}
