package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.input.NumberField;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public final class FloatSettingsEntry extends SettingsEntry<Float> {

    private NumberField numberField;

    public FloatSettingsEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Float value, Consumer<Float> consumer) {
        super(entrance, entryList, title, symbol, value, consumer);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY() + 1, 72, 18));
        numberField.setFilter(NumberField.DOUBLE_FILTER);
        numberField.setNumber(getItem());
        numberField.setResponder(number -> {
            setItem(number.floatValue());
        });

    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

}
