package dev.huskuraft.effortless.screen.transformer.edit;

import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.checkbox.Checkbox;
import dev.huskuraft.effortless.gui.input.NumberField;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

public final class ArrayEditEntry extends TransformerEditEntry<Array> {

    private Checkbox buttonArrayEnabled;
    private NumberField offsetXField, offsetYField, offsetZField, countField;

    public ArrayEditEntry(Entrance entrance, EntryList entryList, Array array) {
        super(entrance, entryList, array);
    }

    @Override
    public void onCreate() {

        var width = 72;
        var offsetComponent = 4;
        var offsetX = getX() + (getWidth() - width * 3 - offsetComponent * 2) / 2;
        var offsetY = getY() + 1;

        addWidget(new TextWidget(getEntrance(), offsetX + 1, offsetY, Text.text("Position (XYZ)").withStyle(TextStyle.GRAY)));
        offsetY += 11;
        offsetXField = addWidget(new NumberField(getEntrance(), offsetX, offsetY, width, 18));

        offsetXField.setTooltipMessage(Text.text("How much each copy is shifted."));
        offsetXField.setFilter(NumberField.DOUBLE_FILTER);
        offsetXField.setNumber(getItem().x());
        offsetXField.setResponder(number -> {
            item = getItem().withOffsetX(number.doubleValue());
        });

        offsetYField = addWidget(new NumberField(getEntrance(), offsetX + 76, offsetY, width, 18));
        offsetYField.setTooltipMessage(Text.text("How much each copy is shifted."));
        offsetYField.setFilter(NumberField.DOUBLE_FILTER);
        offsetYField.setNumber(getItem().y());
        offsetYField.setResponder(number -> {
            item = getItem().withOffsetY(number.doubleValue());
        });

        offsetZField = addWidget(new NumberField(getEntrance(), offsetX + 76 * 2, offsetY, width, 18));
        offsetZField.setTooltipMessage(Text.text("How much each copy is shifted."));
        offsetZField.setFilter(NumberField.DOUBLE_FILTER);
        offsetZField.setNumber(getItem().z());
        offsetZField.setResponder(number -> {
            item = getItem().withOffsetZ(number.doubleValue());
        });

        offsetY += 22;
        addWidget(new TextWidget(getEntrance(), offsetX + 1, offsetY, Text.text("Count").withStyle(TextStyle.GRAY)));
        offsetY += 11;
        countField = addWidget(new NumberField(getEntrance(), offsetX, offsetY, width, 18));
        countField.setTooltip(Text.text("How many copies should be made."));
        countField.setFilter(NumberField.INTEGER_FILTER);
        countField.setNumber(getItem().count());
        countField.setResponder(number -> {
            item = getItem().withCount(number.intValue());
        });
    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return 68;
    }

    @Override
    public Text getNarration() {
        return Text.empty();
    }

    @Override
    public void setItem(Array item) {
        super.setItem(item);
    }
}
