package dev.huskuraft.effortless.screen.settings;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.Dimens;
import dev.huskuraft.universal.api.gui.EntryList;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.container.AbstractEntryList;
import dev.huskuraft.universal.api.gui.container.EditableEntryList;
import dev.huskuraft.universal.api.gui.input.NumberField;
import dev.huskuraft.universal.api.gui.slot.TextSlot;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;

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

    public boolean isShowIcon() {
        return showIcon;
    }

    public void setShowIcon(boolean visible) {
        this.showIcon = visible;
        this.recreateChildren();
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean visible) {
        this.showButton = visible;
        this.recreateChildren();
    }

    public PositionEntry addPositionEntry(Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
        return addEntry(new PositionEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public NumberEntry addNumberEntry(Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
        return addEntry(new NumberEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public IntegerEntry addIntegerEntry(Text title, Text symbol, int value, int min, int max, Consumer<Integer> consumer) {
        return addEntry(new IntegerEntry(getEntrance(), this, title, symbol, value, min, max, consumer));
    }

    public IntegerEntry addIntegerEntry(Text title, Text symbol, int value, int min, int max, int step, Consumer<Integer> consumer) {
        return addEntry(new IntegerEntry(getEntrance(), this, title, symbol, value, min, max, step, consumer));
    }

    public SelectorEntry<Boolean> addSwitchEntry(Text title, Text symbol, Boolean value, Consumer<Boolean> consumer) {
        return addEntry(new SelectorEntry<>(getEntrance(), this, title, symbol, List.of(
                Text.translate("effortless.option.on").withStyle(ChatFormatting.GREEN),
                Text.translate("effortless.option.off").withStyle(ChatFormatting.RED)
        ), List.of(Boolean.TRUE, Boolean.FALSE), value, consumer));
    }

    public <T> SelectorEntry<T> addSelectorEntry(Text title, Text symbol, List<Text> messages, List<T> values, T value, Consumer<T> consumer) {
        return addEntry(new SelectorEntry<>(getEntrance(), this, title, symbol, messages, values, value, consumer));
    }

    public <T> ButtonEntry<T> addTab(Text title, Text symbol, T value, Consumer<T> consumer, BiConsumer<ButtonEntry<T>, T> buttonConsumer) {
        return addEntry(new ButtonEntry<>(getEntrance(), this, title, symbol, value, consumer, buttonConsumer));
    }


    public static final class ButtonEntry<T> extends SettingsEntry<T> {

        private final BiConsumer<ButtonEntry<T>, T> entryConsumer;
        private Button button;

        public ButtonEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, T value, Consumer<T> consumer, BiConsumer<ButtonEntry<T>, T> entryConsumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.entryConsumer = entryConsumer;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            this.button = addWidget(new Button(getEntrance(), getInnerRight() - Button.BUTTON_WIDTH_1, getTop(), Button.BUTTON_WIDTH_1, Button.DEFAULT_HEIGHT, Text.empty()));
            this.entryConsumer.accept(this, getItem());
            this.titleTextWidget.setWidth(getInnerRight() - getInnerLeft() - 8 - button.getWidth());
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

    public static final class PositionEntry extends SettingsEntry<Double> {

        private final Double min;
        private final Double max;
        private NumberField numberField;
        private Button positionRoundButton;

        public PositionEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, Double value, Double min, Double max, Consumer<Double> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            this.numberField = addWidget(new NumberField(getEntrance(), getInnerRight() - 72 - 20, getTop(), 72, 20, NumberField.TYPE_DOUBLE));
            this.numberField.setValueRange(min, max);
            this.numberField.setValue(getItem());
            this.numberField.setValueChangeListener(value -> {
                super.setItem(value.doubleValue());
            });
            this.positionRoundButton = addWidget(new Button(getEntrance(), getInnerRight() - 20, getTop(), 20, 20, Text.empty()));
            this.titleTextWidget.setWidth(getInnerRight() - getInnerLeft() - 8 - numberField.getWidth());

        }

        @Override
        public void setItem(Double item) {
            super.setItem(item);
            this.numberField.setValue(getItem());
        }

        @Override
        public void onReload() {
            if (Math.round(getItem() * 2) / 2.0 % 1 == 0) {
                this.positionRoundButton.setMessage(".0");
                this.positionRoundButton.setOnPressListener(button -> {
                    setItem(Math.floor(getItem()) + 0.5);
                });
            } else {
                this.positionRoundButton.setMessage(".5");
                this.positionRoundButton.setOnPressListener(button -> {
                    setItem(Math.floor(getItem()));
                });
            }
        }
    }

    public static final class NumberEntry extends SettingsEntry<Double> {

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
            this.titleTextWidget.setWidth(getInnerRight() - getInnerLeft() - 8 - numberField.getWidth());

        }

        @Override
        public void setItem(Double item) {
            super.setItem(item);
            this.numberField.setValue(getItem());
        }
    }

    public static final class IntegerEntry extends SettingsEntry<Integer> {

        private final int min;
        private final int max;
        private final int step;
        private NumberField numberField;

        public IntegerEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, Integer value, int min, int max, Consumer<Integer> consumer) {
            this(entrance, entryList, title, symbol, value, min, max, 1, consumer);
        }

        public IntegerEntry(Entrance entrance, SettingOptionsList entryList, Text title, Text symbol, Integer value, int min, int max, int step, Consumer<Integer> consumer) {
            super(entrance, entryList, title, symbol, value, consumer);
            this.min = min;
            this.max = max;
            this.step = step;
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
            this.numberField.setStep(step);
            this.titleTextWidget.setWidth(getInnerRight() - getInnerLeft() - 8 - numberField.getWidth());

        }

        @Override
        public void setItem(Integer item) {
            super.setItem(item);
            this.numberField.setValue(getItem());
        }
    }

    public static final class SelectorEntry<T> extends SettingsEntry<T> {

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

            this.button = addWidget(new Button(getEntrance(), getInnerRight() - Button.BUTTON_WIDTH_1, getTop(), Button.BUTTON_WIDTH_1, Button.DEFAULT_HEIGHT, getButtonMessage()));
            this.button.setOnPressListener(button -> {
                setItem(getNextItem());
            });
            this.titleTextWidget.setWidth(getInnerRight() - getInnerLeft() - 8 - button.getWidth());

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
            this.textSlot = addWidget(new TextSlot(getEntrance(), getLeft() + 1, getTop() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, Text.empty(), getSymbol()));
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


}
