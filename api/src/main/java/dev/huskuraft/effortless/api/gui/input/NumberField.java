package dev.huskuraft.effortless.api.gui.input;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class NumberField extends AbstractContainerWidget {

    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_DOUBLE = 1;
    private final int buttonWidth = 10;
    private final EditBox textField;
    private final Button minusButton;
    private final Button plusButton;
    private final NumberFormat format;
    private final int type;
    private final int ctrlPressedStep = 5;
    private final int shiftPressedStep = 10;
    private Range range;

    public NumberField(Entrance entrance, int x, int y, int width, int height, int type) {
        super(entrance, x, y, width, height, Text.empty());
        this.format = new DecimalFormat();
        this.format.setGroupingUsed(false);

        if (type != TYPE_DOUBLE && type != TYPE_INTEGER) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        this.type = type;

        this.textField = addWidget(new EditBox(entrance, x + buttonWidth, y + 1, width - 2 * buttonWidth, height - 2, Text.empty()));
        this.minusButton = addWidget(new Button(entrance, x, y, buttonWidth, height, Text.text("-"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().getWindow().isControlDown()) valueChanged = ctrlPressedStep;
            if (getEntrance().getClient().getWindow().isShiftDown()) valueChanged = shiftPressedStep;

            setValue(getNumber().doubleValue() - valueChanged);
        }));
        this.plusButton = addWidget(new Button(entrance, x + width - buttonWidth, y, buttonWidth, height, Text.text("+"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().getWindow().isControlDown()) valueChanged = ctrlPressedStep;
            if (getEntrance().getClient().getWindow().isShiftDown()) valueChanged = shiftPressedStep;

            setValue(getNumber().doubleValue() + valueChanged);
        }));
        this.textField.setFilter(text -> {
            if (text.isEmpty() || text.equals("-")) {
                return true;
            }
            try {
                var value = format.parse(text);
                if (range.isBelow(value)) {
                    setValue(range.min());
                    return false;
                } else if (range.isAbove(value)) {
                    setValue(range.max());
                    return false;
                }
            } catch (ParseException e) {
            }
            try {
                return switch (type) {
                    case TYPE_DOUBLE ->
                            Double.parseDouble(text) == BigDecimal.valueOf(Double.parseDouble(text)).setScale(3, RoundingMode.DOWN).doubleValue();
                    case TYPE_INTEGER -> Integer.parseInt(text) == Integer.parseInt(text);
                    default -> false;
                };
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.setTooltipMessage(null);
        this.range = Range.UNBOUNDED;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        this.textField.setActive(active);
        this.minusButton.setActive(active);
        this.plusButton.setActive(active);
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
        textField.setValue(number == null ? "" : format.format(number.doubleValue()));
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

    public void setValueRange(Number min, Number max) {
        this.range = new Range(min, max);
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

    private record Range(Number min, Number max) {

        public static final Range UNBOUNDED = new Range(Integer.MIN_VALUE, Integer.MAX_VALUE);

        public boolean contains(int number) {
            return (number >= min.intValue() && number <= max.intValue());
        }

        public boolean contains(double number) {
            return (number >= min.doubleValue() && number <= max.doubleValue());
        }

        public boolean isBelow(Number number) {
            return (number.doubleValue() < min.doubleValue());
        }

        public boolean isAbove(Number number) {
            return (number.doubleValue() > max.doubleValue());
        }
    }

}
