package dev.huskuraft.effortless.api.gui.slot;

import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public abstract class Slot extends AbstractWidget {

    public Slot(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    public abstract int getFullWidth();

}
