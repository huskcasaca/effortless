package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.screen.transformer.edit.*;
import dev.huskuraft.effortless.screen.transformer.tooltip.*;

public final class TransformerList extends EditableEntryList<Transformer> {

    private final Screen screen;
    @Deprecated
    private final boolean isMovable;
    private final EntryType type;

    public TransformerList(Entrance entrance, int x, int y, int width, int height, Screen screen, boolean isMovable, EntryType type) {
        super(entrance, x, y, width, height);
        this.screen = screen;
        this.isMovable = isMovable;
        this.type = type;
//        this.setRenderSelection(false);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @Override
    protected EditableEntry<Transformer> createHolder(Transformer transformer) {
        return switch (type) {
            case TOOLTIP -> createTooltipHolder(transformer);
            case EDITOR -> createEditorHolder(transformer);
            case FOCUS -> null;
        };
    }

    @SuppressWarnings("unchecked")
    public EditableEntry<Transformer> createTooltipHolder(Transformer transformer) {
        return (EditableEntry<Transformer>) switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerTooltipEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerTooltipEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerTooltipEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerTooltipEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @SuppressWarnings("unchecked")
    private EditableEntry<Transformer> createEditorHolder(Transformer transformer) {
        return (EditableEntry<Transformer>) switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerEditEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerEditEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerEditEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerTooltipEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public enum EntryType {
        TOOLTIP,
        EDITOR,
        FOCUS
    }

}
