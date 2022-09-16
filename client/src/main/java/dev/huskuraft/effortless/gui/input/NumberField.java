package dev.huskuraft.effortless.gui.input;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class NumberField extends AbstractContainerWidget {

    public static final Predicate<String> DOUBLE_FILTER = text -> {
        try {
            if (text.isEmpty()) {
                return true;
            }
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
    public static final Predicate<String> INTEGER_FILTER = text -> {
        try {
            if (text.isEmpty()) {
                return true;
            }
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
    private final int buttonWidth = 10;
    private final EditBox textField;
    private final Button minusButton;
    private final Button plusButton;
    private final NumberFormat format;
    List<Text> tooltip = new ArrayList<>();

    public NumberField(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height, Text.empty());
        this.format = DecimalFormat.getInstance();

        this.textField = addWidget(new EditBox(entrance, x + buttonWidth + 1, y + 1, width - 2 * buttonWidth - 2, height - 2, Text.empty()));
        this.minusButton = addWidget(new Button(entrance, x, y - 1, buttonWidth, height + 2, Text.text("-"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().hasControlDown()) valueChanged = 5f;
            if (getEntrance().getClient().hasShiftDown()) valueChanged = 10f;

            setNumber(getNumber() - valueChanged);
        }));
        this.plusButton = addWidget(new Button(entrance, x + width - buttonWidth, y - 1, buttonWidth, height + 2, Text.text("+"), button -> {
            float valueChanged = 1f;
            if (getEntrance().getClient().hasControlDown()) valueChanged = 5f;
            if (getEntrance().getClient().hasShiftDown()) valueChanged = 10f;

            setNumber(getNumber() + valueChanged);
        }));
        this.setTooltipMessage(null);
    }

    public EditBox getTextField() {
        return textField;
    }

    public double getNumber() {
        if (textField.getValue().isEmpty()) return 0;
        try {
            return format.parse(textField.getValue()).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    public void setNumber(Number number) {
        textField.setValue(format.format(number));
    }

    public void setFilter(Predicate<String> filter) {
        textField.setFilter(filter);
    }

    public void setResponder(Consumer<Number> responder) {
        textField.setResponder(str -> {
            try {
                responder.accept(format.parse(str).doubleValue());
            } catch (ParseException e) {
                setNumber(0.0);
                responder.accept(0.0);
            }
        });
    }

    public void setTooltipMessage(Text tooltip) {
        this.minusButton.setTooltip(
                Stream.of(
                        tooltip,
                        Text.text("Hold ").append(Text.text("shift ").withStyle(TextStyle.AQUA)).append(Text.text("for "))
                                .append(Text.text("10").withStyle(TextStyle.RED)),
                        Text.text("Hold ").append(Text.text("ctrl ").withStyle(TextStyle.AQUA)).append(Text.text("for "))
                                .append(Text.text("5").withStyle(TextStyle.RED))
                ).filter(Objects::nonNull).toList()
        );
        this.textField.setTooltip(
                Stream.of(
                        tooltip
                ).filter(Objects::nonNull).toList()
        );
        this.plusButton.setTooltip(
                Stream.of(
                        tooltip,
                        Text.text("Hold ").append(Text.text("shift ").withStyle(TextStyle.DARK_GREEN)).append(Text.text("for "))
                                .append(Text.text("10").withStyle(TextStyle.RED)),
                        Text.text("Hold ").append(Text.text("ctrl ").withStyle(TextStyle.DARK_GREEN)).append(Text.text("for "))
                                .append(Text.text("5").withStyle(TextStyle.RED))
                ).filter(Objects::nonNull).toList()
        );
    }

}
