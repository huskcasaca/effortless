package dev.huskuraft.effortless.screen.item;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;

public final class ItemStackList extends EditableEntryList<ItemStack> {

    public ItemStackList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @Override
    protected EditableEntry<ItemStack> createHolder(ItemStack item) {
        return new ItemStackEntry(getEntrance(), item);
    }

}
