package dev.huskuraft.effortless.api.gui.container;

import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.Widget;
import dev.huskuraft.effortless.core.Entrance;

import java.util.Collection;
import java.util.List;

public abstract class EditableEntryList<T> extends AbstractEntryList<EditableEntryList.Entry<T>> {

    protected EditableEntryList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    protected abstract Entry<T> createHolder(T item);

    public boolean hasSelected() {
        return getSelected() != null;
    }

    // old: add
    public void insertSelected(T item) {
        var entry = createHolder(item);
        var index = getSelected() == null ? children().size() : children().indexOf(getSelected()) + 1;
        addEntry(index, entry);
        setSelected(entry);
    }

    // old: edit
    public void replaceSelect(T item) {
        getSelected().setItem(item);
    }

    public void deleteSelected() {
        var selected = getSelected();
        var index = children().indexOf(selected);
        removeEntry(selected);
        if (index >= 0 && index < children().size()) {
            setSelected(children().get(index));
        }
    }

    public Entry<T> get(int i) {
        return children().get(i);
    }

    public Entry<T> getSelected() {
        return super.getSelected();
    }

    public int indexOf(Object entry) {
        return children().indexOf(entry);
    }

    public int indexOfSelected() {
        return children().indexOf(getSelected());
    }

    public void moveUp(int i) {
        swap(i, i - 1);
    }

    public void moveDown(int i) {
        swap(i, i + 1);
    }

    public void moveUpSelected() {
        moveUp(indexOfSelected());
    }

    public void moveDownSelected() {
        moveDown(indexOfSelected());
    }

    @Override
    public void moveUp(Widget widget) {
        var index = indexOf(widget);
        if (index > 0) {
            moveUp(index);
            setScrollAmountNoClamp(getScrollAmount() - getOrThrow(index).getHeight());
        }
    }

    @Override
    public void moveDown(Widget widget) {
        var index = indexOf(widget);
        if (index < children().size() - 1) {
            moveDown(index);
            setScrollAmountNoClamp(getScrollAmount() + getOrThrow(index).getHeight());
        }
    }

    @Override
    public void moveUpNoClamp(Widget widget) {
        var index = indexOf(widget);
        if (index > 0) {
            moveUp(indexOf(widget));
            setScrollAmountNoClamp(getScrollAmount() - getOrThrow(index).getHeight());
        }
    }

    @Override
    public void moveDownNoClamp(Widget widget) {
        var index = indexOf(widget);
        if (index < children().size() - 1) {
            moveDown(indexOf(widget));
            setScrollAmountNoClamp(getScrollAmount() + getOrThrow(index).getHeight());
        }
    }

    public void reset(Collection<? extends T> items) {
        int i = indexOf(getSelected());
        clearEntries();

        for (T item : items) {
            addEntry(createHolder(item));
        }

        var list = children();
        if (i >= 0 && i < list.size()) {
            setSelected(list.get(i));
        }
    }

    public List<T> items() {
        return children().stream().map(entry -> entry.item).toList();
    }

    public abstract static class Entry<T> extends AbstractEntryList.Entry {

        private final EntryList entryList;
        protected T item;

        protected Entry(Entrance entrance, T item) {
            this(entrance, null, item);
        }

        protected Entry(Entrance entrance, EntryList entryList, T item) {
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
}