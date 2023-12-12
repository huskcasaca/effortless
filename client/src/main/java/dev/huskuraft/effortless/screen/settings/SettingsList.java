package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.gui.input.NumberField;
import dev.huskuraft.effortless.gui.slot.TextSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public class SettingsList extends AbstractEntryList<SettingsList.Entry<?>> {

    public SettingsList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    public void addFloatEntry(Text title, Text symbol, Float value, Consumer<Float> consumer) {
        addEntry(new FloatEntry(getEntrance(), this, title, symbol, value, consumer));
    }

    public void addDoubleEntry(Text title, Text symbol, Double value, Consumer<Double> consumer) {
        addEntry(new DoubleEntry(getEntrance(), this, title, symbol, value, consumer));
    }

    public void addIntegerEntry(Text title, Text symbol, Integer value, Consumer<Integer> consumer) {
        addEntry(new IntegerEntry(getEntrance(), this, title, symbol, value, consumer));
    }

    public static final class DoubleEntry extends Entry<Double> {

        private NumberField numberField;

        public DoubleEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Double value, Consumer<Double> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
        }

        @Override
        public void onCreate() {
            super.onCreate();

            numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY() + 1, 72, 18));
            numberField.setFilter(NumberField.DOUBLE_FILTER);
            numberField.setNumber(getItem());
            numberField.setResponder(number -> {
                setItem(number.doubleValue());
            });

        }

    }

    public static final class FloatEntry extends Entry<Float> {

        private NumberField numberField;

        public FloatEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Float value, Consumer<Float> consumer) {
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

    }

    public static final class IntegerEntry extends Entry<Integer> {

        private NumberField numberField;

        public IntegerEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Integer value, Consumer<Integer> consumer) {
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

    }

    public abstract static class Entry<T extends Object> extends EditableEntryList.Entry<T> {

        protected TextSlot textSlot;
        protected AbstractWidget titleTextWidget;
        private Text symbol;
        private Consumer<T> consumer;

        protected Entry(Entrance entrance, EntryList entryList, Text title, Text symbol, T value, Consumer<T> consumer) {
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
        public int getWidth() {
            return Dimens.RegularEntry.ROW_WIDTH;
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
}
