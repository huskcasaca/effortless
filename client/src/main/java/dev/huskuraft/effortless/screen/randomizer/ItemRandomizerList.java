package dev.huskuraft.effortless.screen.randomizer;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;

public final class ItemRandomizerList extends EditableEntryList<ItemRandomizer> {

    public ItemRandomizerList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected EditableEntry<ItemRandomizer> createHolder(ItemRandomizer item) {
        return new ItemRandomizerEntry(getEntrance(), this, item);
    }

}
