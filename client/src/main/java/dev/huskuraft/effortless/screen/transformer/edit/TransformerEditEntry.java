package dev.huskuraft.effortless.screen.transformer.edit;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.text.Text;

public abstract class TransformerEditEntry<T extends Transformer> extends EditableEntry<T> {

    public TransformerEditEntry(Entrance entrance, EntryList entryList, T transformer) {
        super(entrance, entryList, transformer);
    }

    @Override
    public Text getNarration() {
        return getItem().getName();
    }
}
