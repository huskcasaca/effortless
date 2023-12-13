package dev.huskuraft.effortless.gui.input;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.text.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Consumer;

public class NumberField extends AbstractContainerWidget {

    private record Range(Number low, Number high) {

        public static final Range UNBOUNDED = new Range(Integer.MIN_VALUE, Integer.MAX_VALUE);

        public boolean contains(int number) {
            return (number >= low.intValue() && number <= high.intValue());
        }

        public boolean contains(double number) {
            return (number >= low.doubleValue() && number <= high.doubleValue());
        }

        public boolean isBelow(Number number) {
            return (number.doubleValue() < low.doubleValue());
        }

        public boolean isAbove(Number number) {
            return (number.doubleValue() > high.doubleValue());
        }
    }

    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_DOUBLE = 1;

    private final int buttonWidth = 10;
    private final EditBox textField;
    private final Button minusButton;
    private final Button plusButton;
    private final NumberFormat format;
    private final int type;
    private Range range;

    public NumberField(Entrance entrance, int x, int y, int width, int height, int type) {
        super(entrance, x, y, width, height, Text.empty());
        this.format = new DecimalFormat();
        this.format.setGroupingUsed(false);

        if (type != TYPE_DOUBLE && type != TYPE_INTEGER) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        this.type = type;

        this.textField = addWidget(new EditBox(entrance, x + buttonWidth + 1, y + 1, width - 2 * buttonWidth - 2, height - 2, Text.empty()));
        this.minusButton = addWidget(new Button(entrance, x, y - 1, buttonWidth, height + 2, Text.text("-"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().hasControlDown()) valueChanged = 5f;
            if (getEntrance().getClient().hasShiftDown()) valueChanged = 10f;

            setValue(getNumber().doubleValue() - valueChanged);
        }));
        this.plusButton = addWidget(new Button(entrance, x + width - buttonWidth, y - 1, buttonWidth, height + 2, Text.text("+"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().hasControlDown()) valueChanged = 5f;
            if (getEntrance().getClient().hasShiftDown()) valueChanged = 10f;

            setValue(getNumber().doubleValue() + valueChanged);
        }));
        this.textField.setFilter(text -> {
            if (text.isEmpty() || text.equals("-")) {
                return true;
            }
            try {
                var value = format.parse(text);
                if (range.isBelow(value)) {
                    setValue(range.low());
                    return false;
                } else if (range.isAbove(value)) {
                    setValue(range.high());
                    return false;
                }
            } catch (ParseException e) {
            }
            try {
                switch (type) {
                    case TYPE_DOUBLE -> Double.parseDouble(text);
                    case TYPE_INTEGER -> Integer.parseInt(text);
                    default -> {
                        return false;
                    }
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.setTooltipMessage(null);
        this.range = Range.UNBOUNDED;
    }

    public Number getNumber() {
        if (textField.getValue().isEmpty()) return 0;
        try {
            return format.parse(textField.getValue());
        } catch (ParseException e) {
            return 0;
        }
    }

    public void setValue(Number number) {
        textField.setValue(format.format(number.doubleValue()));
    }

    public void setValueChangeListener(Consumer<Number> responder) {
        textField.setResponder(text -> {
            if (text.isEmpty() || text.equals("-")) {
                responder.accept(0);
            } else {
                try {
                    responder.accept(format.parse(text));
                } catch (ParseException e) {
                }
            }
        });
    }

    public void setValueRange(Number low, Number high) {
        this.range = new Range(low, high);
    }

    public void setTooltipMessage(Text tooltip) {
//        this.minusButton.setTooltip(
//                Stream.of(
//                        tooltip,
//                        Text.text("Hold ").append(Text.text("shift ").withStyle(TextStyle.AQUA)).append(Text.text("for "))
//                                .append(Text.text("10").withStyle(TextStyle.RED)),
//                        Text.text("Hold ").append(Text.text("ctrl ").withStyle(TextStyle.AQUA)).append(Text.text("for "))
//                                .append(Text.text("5").withStyle(TextStyle.RED))
//                ).filter(Objects::nonNull).toList()
//        );
//        this.textField.setTooltip(
//                Stream.of(
//                        tooltip
//                ).filter(Objects::nonNull).toList()
//        );
//        this.plusButton.setTooltip(
//                Stream.of(
//                        tooltip,
//                        Text.text("Hold ").append(Text.text("shift ").withStyle(TextStyle.DARK_GREEN)).append(Text.text("for "))
//                                .append(Text.text("10").withStyle(TextStyle.RED)),
//                        Text.text("Hold ").append(Text.text("ctrl ").withStyle(TextStyle.DARK_GREEN)).append(Text.text("for "))
//                                .append(Text.text("5").withStyle(TextStyle.RED))
//                ).filter(Objects::nonNull).toList()
//        );
    }

}
