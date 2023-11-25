package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.Widget;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.screen.transformer.edit.*;
import dev.huskuraft.effortless.screen.transformer.info.*;
import dev.huskuraft.effortless.screen.transformer.multi.TransformerMultiEntry;

public final class TransformerList extends EditableEntryList<Transformer> {

    private final Screen screen;
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
            case INFO -> createInfoHolder(transformer);
            case EDIT -> createEditHolder(transformer);
            case FOCUS ->
                    new TransformerMultiEntry<>(getEntrance(), this, createInfoHolder(transformer), createEditHolder(transformer), transformer);
        };
    }

    @SuppressWarnings("unchecked")
    public TransformerInfoEntry<Transformer> createInfoHolder(Transformer transformer) {
        return (TransformerInfoEntry<Transformer>) switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerInfoEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerInfoEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerInfoEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerInfoEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @SuppressWarnings("unchecked")
    private TransformerEditEntry<Transformer> createEditHolder(Transformer transformer) {
        return (TransformerEditEntry<Transformer>) switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerEditEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerEditEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerEditEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerEditEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @Override
    public void moveUp(Widget widget) {
        if (isMovable) {
            var index = indexOf(widget);
            if (index > 0) {
                moveUp(index);
                setScrollAmountNoClamp(getScrollAmount() - getOrThrow(index).getHeight());
            }
        }
    }

    @Override
    public void moveDown(Widget widget) {
        if (isMovable) {
            var index = indexOf(widget);
            if (index < children().size() - 1) {
                moveDown(index);
                setScrollAmountNoClamp(getScrollAmount() + getOrThrow(index).getHeight());
            }
        }
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public enum EntryType {
        INFO,
        EDIT,
        FOCUS
    }

}
