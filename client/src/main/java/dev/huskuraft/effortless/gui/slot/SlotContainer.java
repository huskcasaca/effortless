package dev.huskuraft.effortless.gui.slot;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.text.Text;

import java.util.List;

public class SlotContainer extends AbstractContainerWidget {

    private boolean wrapLines = false;

    public SlotContainer(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height, Text.empty());
    }

    public void setEntries(List<SlotData> entries) {
        clearWidgets();
        var width = 0;
        for (int i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);
            if (entry instanceof SlotData.TextSymbol textEntry) {
                var slot = this.addWidget(new TextSlot(getEntrance(), getX() + width, getY(), Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, textEntry.text(), textEntry.symbol()));
                width += slot.getFullWidth() + 2;
            } else if (entry instanceof SlotData.ItemStackSymbol itemEntry) {
                var slot = this.addWidget(new ItemSlot(getEntrance(), getX() + width, getY(), Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, itemEntry.itemStack(), itemEntry.symbol()));
                width += slot.getFullWidth() + 2;
            }
        }
    }

    public void setWrapLines(boolean wrapLines) {
        this.wrapLines = wrapLines;
    }

}
