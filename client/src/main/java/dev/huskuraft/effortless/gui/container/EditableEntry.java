package dev.huskuraft.effortless.gui.container;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;

public abstract class EditableEntry<T> extends AbstractEntry {

    private final EntryList entryList;
    protected T item;

    protected EditableEntry(Entrance entrance, T item) {
        this(entrance, null, item);
    }

    protected EditableEntry(Entrance entrance, EntryList entryList, T item) {
        super(entrance);
        this.entryList = entryList;
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
        onBindItem();
    }

    @Override
    public void onLoad() {
        onBindItem();
    }

    public void onBindItem() {
    }

    public EntryList getEntryList() {
        return entryList;
    }

}
