package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;

public final class PatternList extends EditableEntryList<Pattern> {

    public PatternList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected EditableEntry<Pattern> createHolder(Pattern item) {
        return new PatternEntry(getEntrance(), item);
    }

}
