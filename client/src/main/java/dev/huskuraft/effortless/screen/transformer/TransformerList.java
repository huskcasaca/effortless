package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.building.pattern.mirror.Mirror;
import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.gui.Widget;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.screen.transformer.edit.ArrayEditEntry;
import dev.huskuraft.effortless.screen.transformer.edit.MirrorEditEntry;
import dev.huskuraft.effortless.screen.transformer.edit.RadialEditEntry;
import dev.huskuraft.effortless.screen.transformer.edit.TransformerEditEntry;
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
    private TransformerInfoEntry<Transformer> createInfoHolder(Transformer transformer) {
        return (TransformerInfoEntry) switch (transformer.getType()) {
            case ARRAY -> new ArrayInfoEntry(getEntrance(), this, (Array) transformer);
            case MIRROR -> new MirrorInfoEntry(getEntrance(), this, (Mirror) transformer);
            case RADIAL -> new RadialInfoEntry(getEntrance(), this, (Radial) transformer);
            case RANDOMIZE -> switch (((Randomizer) transformer).getCategory()) {
                case ITEM -> new ItemRandomizerInfoEntry(getEntrance(), this, (ItemRandomizer) transformer);
            };
        };
    }

    @SuppressWarnings("unchecked")
    private TransformerEditEntry<Transformer> createEditHolder(Transformer transformer) {
        return (TransformerEditEntry) switch (transformer.getType()) {
            case ARRAY -> new ArrayEditEntry(getEntrance(), this, (Array) transformer);
            case MIRROR -> new MirrorEditEntry(getEntrance(), this, (Mirror) transformer);
            case RADIAL -> new RadialEditEntry(getEntrance(), this, (Radial) transformer);
            case RANDOMIZE -> switch (((Randomizer) transformer).getCategory()) {
                case ITEM -> new ItemRandomizerInfoEntry(getEntrance(), this, (ItemRandomizer) transformer);
            };
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
