package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public class SettingsList extends AbstractEntryList<SettingsEntry<?>> {

    public SettingsList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    public void addFloatEntry(Text title, Text symbol, Float value, Consumer<Float> consumer) {
        addEntry(new FloatSettingsEntry(getEntrance(), this, title, symbol, value, consumer));
    }

    public void addDoubleEntry(Text title, Text symbol, Double value, Consumer<Double> consumer) {
        addEntry(new DoubleSettingsEntry(getEntrance(), this, title, symbol, value, consumer));
    }

    public void addIntegerEntry(Text title, Text symbol, Integer value, Consumer<Integer> consumer) {
        addEntry(new IntegerSettingsEntry(getEntrance(), this, title, symbol, value, consumer));
    }

}
