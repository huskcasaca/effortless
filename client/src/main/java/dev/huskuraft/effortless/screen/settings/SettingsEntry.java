package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.slot.TextSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public abstract class SettingsEntry<T extends Object> extends EditableEntry<T> {

    protected TextSlot textSlot;
    protected AbstractWidget titleTextWidget;
    private Text symbol;
    private Consumer<T> consumer;

    protected SettingsEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, T value, Consumer<T> consumer) {
        super(entrance, entryList, value);
        setMessage(title);
        this.symbol = symbol;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        this.textSlot = addWidget(new TextSlot(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getSymbol()));
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getMessage()));
    }

    public Text getSymbol() {
        return symbol;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public void setItem(T item) {
        super.setItem(item);
        consumer.accept(item);
    }

}
