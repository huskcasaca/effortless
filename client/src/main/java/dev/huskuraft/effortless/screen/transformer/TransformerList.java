package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;

public final class TransformerList extends EditableEntryList<Transformer> {

    public TransformerList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected EditableEntry<Transformer> createHolder(Transformer transformer) {
        return (EditableEntry<Transformer>) switch (transformer.getType()) {
            case ARRAY ->
                    new TransformerEntry.ArrayTransformerEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR ->
                    new TransformerEntry.MirrorTransformerEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL ->
                    new TransformerEntry.RadialTransformerEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND ->
                    new TransformerEntry.ItemRandomizerEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @Override
    public boolean isEditable() {
        return true;
    }

}
