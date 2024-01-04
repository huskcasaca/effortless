package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Entrance;
import dev.huskuraft.effortless.api.core.Tuple2;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.input.NumberField;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.PositionType;

import java.util.List;
import java.util.function.Consumer;

public class SettingsList extends AbstractEntryList<SettingsList.Entry<?>> {

    public SettingsList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    public PositionEntry addPositionEntry(Axis axis, Tuple2<PositionType, Double> value, Consumer<Tuple2<PositionType, Double>> consumer) {
        return (PositionEntry) addEntry(new PositionEntry(getEntrance(), this, axis, value, consumer));
    }

    public DoubleEntry addDoubleEntry(Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
        return (DoubleEntry) addEntry(new DoubleEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public IntegerEntry addIntegerEntry(Text title, Text symbol, Integer value, Integer min, Integer max, Consumer<Integer> consumer) {
        return (IntegerEntry) addEntry(new IntegerEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public <T> SelectorEntry<T> addValuesEntry(Text title, Text symbol, List<Text> messages, List<T> values, int index, Consumer<T> consumer) {
        return (SelectorEntry<T>) addEntry(new SelectorEntry<>(getEntrance(), this, title, symbol, messages, values, index, consumer));
    }

    public static final class PositionEntry extends Entry<Tuple2<PositionType, Double>> {

        private Button typeButton;
        private NumberField numberField;
        private Button teleportButton;
        private Axis axis;

        public PositionEntry(Entrance entrance, EntryList entryList, Axis axis, Tuple2<PositionType, Double> value, Consumer<Tuple2<PositionType, Double>> consumer) {
            super(entrance, entryList, Text.translate("effortless.position", axis.getDisplayName()), axis.getDisplayName(), value, consumer);
            this.axis = axis;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.typeButton = addWidget(new Button(getEntrance(), getX() + getWidth() - 60, getY(), 60, 20, getItem().value1().getDisplayName()));
            this.typeButton.setOnPressListener(button -> {
                setItem(getItem().withValue1(PositionType.values()[(getItem().value1().ordinal() + 1) % PositionType.values().length]).withValue2(0d));
                numberField.setValue(getItem().value2());
                typeButton.setMessage(getItem().value1().getDisplayName());
                teleportButton.setVisible(getItem().value1() == PositionType.ABSOLUTE);
            });

            this.numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 60 - 72, getY(), 72, 20, NumberField.TYPE_DOUBLE));
            this.numberField.setValueRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.numberField.setValue(getItem().value2());
            this.numberField.setValueChangeListener(value -> {
                setItem(getItem().withValue2(value.doubleValue()));
            });

            this.teleportButton = addWidget(new Button(getEntrance(), getX() + getWidth() - 60 - 72 - 20, getY(), 20, 20, Text.text("P")));
            this.teleportButton.setVisible(getItem().value1() == PositionType.ABSOLUTE);
            this.teleportButton.setOnPressListener(button -> {
                setItem(getItem().withValue2(switch (axis) {
                    case X -> getEntrance().getClient().getPlayer().getPosition().x();
                    case Y -> getEntrance().getClient().getPlayer().getPosition().y();
                    case Z -> getEntrance().getClient().getPlayer().getPosition().z();
                }));
                numberField.setValue(getItem().value2());
            });

        }

        public void setAxis(Axis axis) {
            this.axis = axis;
            setMessage(Text.translate("effortless.position", axis.getDisplayName()));
            setSymbol(axis.getDisplayName());
        }
    }

    public static final class DoubleEntry extends Entry<Double> {

        private final Double min;
        private final Double max;
        private NumberField numberField;

        public DoubleEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY(), 72, 20, NumberField.TYPE_DOUBLE));
            this.numberField.setValueRange(min, max);
            this.numberField.setValue(getItem());
            this.numberField.setValueChangeListener(value -> {
                setItem(value.doubleValue());
            });

        }

    }

    public static final class IntegerEntry extends Entry<Integer> {

        private final Integer min;
        private final Integer max;
        private NumberField numberField;

        public IntegerEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, Integer value, Integer min, Integer max, Consumer<Integer> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY(), 72, 20, NumberField.TYPE_INTEGER));
            this.numberField.setValueRange(min, max);
            this.numberField.setValue(getItem());
            this.numberField.setValueChangeListener(value -> {
                setItem(value.intValue());
            });

        }

    }

    public static final class SelectorEntry<T> extends Entry<T> {

        private final List<Text> messages;
        private final List<T> values;
        private Button button;
        private int index;

        public SelectorEntry(Entrance entrance, EntryList entryList, Text title, Text symbol, List<Text> messages, List<T> values, int index, Consumer<T> consumer) {
            super(entrance, entryList, title, symbol, values.get(index), consumer);
            this.messages = messages;
            this.values = values;
            this.index = index;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.button = addWidget(new Button(getEntrance(), getX() + getWidth() - 60, getY(), 60, 20, messages.get(index)));
            this.button.setOnPressListener(button -> {
                index = (index + 1) % messages.size();
                button.setMessage(messages.get(index));
                setItem(values.get(index));
            });

        }

    }

    public abstract static class Entry<T> extends EditableEntryList.Entry<T> {

        protected TextSlot textSlot;
        protected AbstractWidget titleTextWidget;
        private Text symbol;
        private Consumer<T> consumer;

        protected Entry(Entrance entrance, EntryList entryList, Text title, Text symbol, T value, Consumer<T> consumer) {
            super(entrance, entryList, value);
            super.setMessage(title);
            this.symbol = symbol;
            this.consumer = consumer;
        }

        @Override
        public void onCreate() {
            this.textSlot = addWidget(new TextSlot(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getSymbol()));
            this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getMessage()));
        }

        @Override
        public void setMessage(Text text) {
            super.setMessage(text);
            this.titleTextWidget.setMessage(getMessage());
        }

        public Text getSymbol() {
            return symbol;
        }

        public void setSymbol(Text symbol) {
            this.symbol = symbol;
            this.textSlot.setMessage(getSymbol());
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
