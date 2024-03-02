package dev.huskuraft.effortless.api.gui.container;

import java.util.Comparator;

import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.Widget;
import dev.huskuraft.effortless.api.platform.Entrance;

public class SimpleEntryList extends AbstractEntryList<SimpleEntryList.Entry> {

    public SimpleEntryList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    public static class Entry extends AbstractEntryList.Entry {

        protected Entry(Entrance entrance) {
            super(entrance);
        }

        @Override
        public int getWidth() {
            return Dimens.RegularEntry.ROW_WIDTH;
        }

        @Override
        public int getHeight() {
            return children().stream().map(Widget::getBottom).max(Comparator.naturalOrder()).orElse(0) - children().stream().map(Widget::getTop).min(Comparator.naturalOrder()).orElse(0) + 4;
        }
    }
}
