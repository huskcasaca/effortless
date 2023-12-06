package dev.huskuraft.effortless.gui.slot;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.text.Text;

public abstract class Slot extends AbstractWidget {

    public Slot(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    public abstract int getFullWidth();

}
