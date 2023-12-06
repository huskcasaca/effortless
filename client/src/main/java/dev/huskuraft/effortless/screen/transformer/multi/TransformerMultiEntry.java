package dev.huskuraft.effortless.screen.transformer.multi;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.screen.transformer.edit.TransformerEditEntry;
import dev.huskuraft.effortless.screen.transformer.tooltip.TransformerTooltipEntry;
import dev.huskuraft.effortless.text.Text;

@Deprecated
public class TransformerMultiEntry<T extends Transformer> extends EditableEntry<T> {

    private final TransformerEditEntry<T> edit;
    private final TransformerTooltipEntry<T> info;

    private EditableEntry<T> lastEntry;

    public TransformerMultiEntry(Entrance entrance, EntryList entryList, TransformerTooltipEntry<T> info, TransformerEditEntry<T> edit, T item) {
        super(entrance, entryList, item);
        this.edit = edit;
        this.info = info;
        this.lastEntry = info;
    }

    @Override
    public void onCreate() {
        edit.onCreate();
        info.onCreate();
        addWidget(edit);
        addWidget(info);
    }

    @Override
    public void onReload() {
        if (getEntryList().getSelected() == this) {
            clearWidgets();
            addWidget(edit);
            lastEntry = edit;
            info.setItem(edit.getItem());
        } else {
            clearWidgets();
            addWidget(info);
            lastEntry = info;
            edit.setItem(edit.getItem());
        }
    }

    @Override
    public Text getNarration() {
        return lastEntry.getNarration();
    }

    @Override
    public int getWidth() {
        return lastEntry.getWidth();
    }

    @Override
    public int getHeight() {
        return lastEntry.getHeight();
    }

    @Override
    public void setX(int x) {
        info.setX(x);
        edit.setX(x);
    }

    @Override
    public void setY(int y) {
        info.setY(y);
        edit.setY(y);
    }

    @Override
    public void moveX(int x) {
        info.moveX(x);
        edit.moveX(x);
    }

    @Override
    public void moveY(int y) {
        info.moveY(y);
        edit.moveY(y);
    }
}
