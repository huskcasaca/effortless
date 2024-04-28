package dev.huskuraft.effortless.screen.settings;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.input.NumberField;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.lang.Tuple2;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.PositionType;

public class SettingOptionsList extends AbstractEntryList<SettingOptionsList.Entry<?>> {

    private boolean showIcon;
    private boolean showButton;

    public SettingOptionsList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
        this.showIcon = false;
        this.showButton = false;
    }

    public SettingOptionsList(Entrance entrance, int x, int y, int width, int height, boolean showIcon, boolean showButton) {
        super(entrance, x, y, width, height);
        this.showIcon = showIcon;
        this.showButton = showButton;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    public void setShowIcon(boolean visible) {
        this.showIcon = visible;
        this.recreateChildren();
    }

    public void setShowButton(boolean visible) {
        this.showButton = visible;
        this.recreateChildren();
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public PositionNumberEntry addPositionNumberEntry(Axis axis, Tuple2<PositionType, Double> value, Consumer<Tuple2<PositionType, Double>> consumer) {
        return addEntry(new PositionNumberEntry(getEntrance(), this, axis, value, consumer));
    }

    public NumberEntry addNumberEntry(Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
        return addEntry(new NumberEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public IntegerEntry addIntegerEntry(Text title, Text symbol, Integer value, Integer min, Integer max, Consumer<Integer> consumer) {
        return addEntry(new IntegerEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public SelectorEntry<Boolean> addSwitchEntry(Text title, Text symbol, Boolean value, Consumer<Boolean> consumer) {
        return addEntry(new SelectorEntry<>(getEntrance(), this, title, symbol, List.of(
                Text.translate("effortless.option.on").withStyle(TextStyle.GREEN),
                Text.translate("effortless.option.off").withStyle(TextStyle.RED)
        ), List.of(Boolean.TRUE, Boolean.FALSE), value, consumer));
    }

    public <T> SelectorEntry<T> addSelectorEntry(Text title, Text symbol, List<Text> messages, List<T> values, T value, Consumer<T> consumer) {
        return addEntry(new SelectorEntry<>(getEntrance(), this, title, symbol, messages, values, value, consumer));
    }

    public <T> ButtonEntry<T> addTab(Text title, Text symbol, T value, Consumer<T> consumer, BiConsumer<ButtonEntry<T>, T> buttonConsumer) {
        return addEntry(new ButtonEntry<>(getEntrance(), this, title, symbol, value, consumer, buttonConsumer));
    }

    public final class PositionNumberEntry extends SettingsEntry<Tuple2<PositionType, Double>> {

        private Button typeButton;
        private NumberField numberField;
        private Button teleportButton;
        private Axis axis;

        public PositionNumberEntry(Entrance entrance, SettingOptionsList entryList, Axis axis, Tuple2<PositionType, Double> value, Consumer<Tuple2<PositionType, Double>> consumer) {
            super(entrance, entryList, Text.translate("effortless.position", axis.getDisplayName()), axis.getDisplayName(), value, consumer);
            this.axis = axis;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.typeButton = addWidget(new Button(getEntrance(), getInnerRight() - 72, getTop(), 72, 20, getItem().value1().getDisplayName()));
            this.typeButton.setOnPressListener(button -> {
                setItem(getItem().withValue1(PositionType.values()[(getItem().value1().ordinal() + 1) % PositionType.values().length]).withValue2(0d));
                numberField.setValue(getItem().value2());
                typeButton.setMessage(getItem().value1().getDisplayName());
                teleportButton.setVisible(getItem().value1() == PositionType.ABSOLUTE);
            });

            this.numberField = addWidget(new NumberField(getEntrance(), getInnerRight() - 72 - 72, getTop(), 72, 20, NumberField.TYPE_DOUBLE));
            this.numberField.setValueRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.numberField.setValue(getItem().value2());
            this.numberField.setValueChangeListener(value -> {
                super.setItem(getItem().withValue2(value.doubleValue()));
            });

            this.teleportButton = addWidget(new Button(getEntrance(), getInnerRight() - 72 - 72 - 20, getTop(), 20, 20, Text.text("P")));
            this.teleportButton.setVisible(getItem().value1() == PositionType.ABSOLUTE);
            this.teleportButton.setOnPressListener(button -> {
                setItem(getItem().withValue2(switch (axis) {
                    case X -> getEntrance().getClient().getPlayer().getPosition().toVector3i().toVector3d().x();
                    case Y -> getEntrance().getClient().getPlayer().getPosition().toVector3i().toVector3d().y();
                    case Z -> getEntrance().getClient().getPlayer().getPosition().toVector3i().toVector3d().z();
                }));
                numberField.setValue(getItem().value2());
            });

        }

        public void setAxis(Axis axis) {
            this.axis = axis;
            setMessage(Text.translate("effortless.position", axis.getDisplayName()));
            setSymbol(axis.getDisplayName());
        }

        @Override
        public void setItem(Tuple2<PositionType, Double> item) {
            super.setItem(item);
            this.typeButton.setMessage(getItem().value1().getDisplayName());
            this.teleportButton.setVisible(getItem().value1() == PositionType.ABSOLUTE);
            this.numberField.setValue(getItem().value2());
        }
    }

    public final class NumberEntry extends SettingsEntry<Double> {

        private final Double min;
        private final Double max;
        private NumberField numberField;

        public NumberEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.numberField = addWidget(new NumberField(getEntrance(), getInnerRight() - 72, getTop(), 72, 20, NumberField.TYPE_DOUBLE));
            this.numberField.setValueRange(min, max);
            this.numberField.setValue(getItem());
            this.numberField.setValueChangeListener(value -> {
                super.setItem(value.doubleValue());
            });

        }

        @Override
        public void setItem(Double item) {
            super.setItem(item);
            this.numberField.setValue(getItem());
        }
    }

    public final class IntegerEntry extends SettingsEntry<Integer> {

        private final Integer min;
        private final Integer max;
        private NumberField numberField;

        public IntegerEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, Integer value, Integer min, Integer max, Consumer<Integer> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.numberField = addWidget(new NumberField(getEntrance(), getInnerRight() - 72, getTop(), 72, 20, NumberField.TYPE_INTEGER));
            this.numberField.setValueRange(min, max);
            this.numberField.setValue(getItem());
            this.numberField.setValueChangeListener(value -> {
                super.setItem(value.intValue());
            });

        }

        @Override
        public void setItem(Integer item) {
            super.setItem(item);
            this.numberField.setValue(getItem());
        }
    }

    public final class SelectorEntry<T> extends SettingsEntry<T> {

        private final List<Text> messages;
        private final List<T> values;
        private Button button;
        private T value;

        public SelectorEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, List<Text> messages, List<T> values, T value, Consumer<T> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            assert messages.size() == values.size();
            this.messages = messages;
            this.values = values;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.button = addWidget(new Button(getEntrance(), getInnerRight() - Button.QUARTER_WIDTH, getTop(), Button.QUARTER_WIDTH, Button.DEFAULT_HEIGHT, getButtonMessage()));
            this.button.setOnPressListener(button -> {
                setItem(getNextItem());
            });

        }

        private int getIndex() {
            return getItem() == null ? -1 : values.indexOf(getItem());
        }
        private T getNextItem() {
            return values.get((getIndex() + 1) % values.size());
        }

        private Text getButtonMessage() {
            var index = getIndex();
            if (index == -1 || index >= messages.size()) {
                return Text.empty();
            }
            return messages.get(index);
        }

        @Override
        public void setItem(T item) {
            super.setItem(item);
            this.button.setMessage(getButtonMessage());
        }

    }

    public abstract static class SettingsEntry<T> extends Entry<T> {

        protected TextSlot textSlot;
        protected AbstractWidget titleTextWidget;
        protected Button altButton;
        private Text symbol;
        private Consumer<T> consumer;

        protected SettingsEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, T value, Consumer<T> consumer) {
            super(entrance, entryList, value);
            super.setMessage(title);
            this.symbol = symbol;
            this.consumer = consumer;
        }

        @Override
        public void onCreate() {
            this.textSlot = addWidget(new TextSlot(getEntrance(), getLeft() + 1, getTop() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getSymbol()));
            this.textSlot.setVisible(this.getEntryList().isShowIcon());

            this.altButton = addWidget(new Button(getEntrance(), getRight() - 20, getTop(), 20, 20, Text.empty()));
            this.altButton.setVisible(this.getEntryList().isShowButton());

            this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getInnerLeft() + 4, getTop() + 6, getMessage()));
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
            this.recreate();
        }

        public Button getAltButton() {
            return altButton;
        }

        public int getInnerLeft() {
            return getEntryList().isShowIcon() ? getLeft() + 20 : getLeft();
        }

        public int getInnerRight() {
            return getEntryList().isShowButton() ? getRight() - 20 : getRight();
        }

        @Override
        public int getWidth() {
            return Dimens.Entry.ROW_WIDTH;
        }

        @Override
        public int getHeight() {
            return 24;
        }

        @Override
        public void setItem(T item) {
            super.setItem(item);
            if (consumer != null) {
                consumer.accept(item);
            }
        }

        @Override
        public SettingOptionsList getEntryList() {
            return (SettingOptionsList) super.getEntryList();
        }


        public void setConsumer(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        public AbstractWidget getTitleTextWidget() {
            return titleTextWidget;
        }
    }

    public static final class ButtonEntry<T>  extends SettingsEntry<T> {

        private final BiConsumer<ButtonEntry<T>, T> entryConsumer;
        private Button button;

        public ButtonEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, T value, Consumer<T> consumer, BiConsumer<ButtonEntry<T>, T> entryConsumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.entryConsumer = entryConsumer;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            this.button = addWidget(new Button(getEntrance(), getInnerRight() - Button.QUARTER_WIDTH, getTop(), Button.QUARTER_WIDTH, Button.DEFAULT_HEIGHT, Text.empty()));
            this.entryConsumer.accept(this, getItem());
        }

        @Override
        public void setItem(T item) {
            super.setItem(item);
            this.entryConsumer.accept(this, item);
        }

        public Button getButton() {
            return button;
        }
    }


    public abstract static class Entry<T> extends EditableEntryList.Entry<T> {

        protected Entry(Entrance entrance, T item) {
            super(entrance, item);
        }

        protected Entry(Entrance entrance, EntryList entryList, T item) {
            super(entrance, entryList, item);
        }
    }




}
