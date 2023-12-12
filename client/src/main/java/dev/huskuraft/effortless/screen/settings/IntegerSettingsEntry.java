package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.input.NumberField;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public final class IntegerSettingsEntry extends SettingsEntry<Integer> {

    private NumberField numberField;

    public IntegerSettingsEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Integer value, Consumer<Integer> consumer) {
        super(entrance, entryList, title, symbol, value, consumer);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY() + 1, 72, 18));
        numberField.setFilter(NumberField.INTEGER_FILTER);
        numberField.setNumber(getItem());
        numberField.setResponder(number -> {
            setItem(number.intValue());
        });

    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

}
