package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;

public final class ItemChanceList extends EditableEntryList<Chance<Item>> {

    public ItemChanceList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @Override
    protected EditableEntry<Chance<Item>> createHolder(Chance<Item> item) {
        return new ItemChanceEntry(getEntrance(), this, item);
    }

    // TODO: 15/9/23 remove
    public int totalCount() {
        return items().stream().mapToInt(Chance::chance).sum();
    }

}
