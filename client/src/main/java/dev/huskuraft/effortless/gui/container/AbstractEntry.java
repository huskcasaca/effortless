package dev.huskuraft.effortless.gui.container;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.gui.EntryEventListener;
import dev.huskuraft.effortless.text.Text;

public abstract class AbstractEntry extends AbstractContainerWidget implements EntryEventListener {

    protected AbstractEntry(Entrance entrance) {
        super(entrance, 0, 0, 0, 0, Text.empty());
    }

    @Override
    public void onPositionChange(int from, int to) {

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onDeselected() {

    }

    @Deprecated
    public abstract Text getNarration();

}
