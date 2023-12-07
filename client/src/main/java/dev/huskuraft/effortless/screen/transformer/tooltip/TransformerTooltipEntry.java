package dev.huskuraft.effortless.screen.transformer.tooltip;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.slot.SlotContainer;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class TransformerTooltipEntry<T extends Transformer> extends EditableEntry<T> {

    protected dev.huskuraft.effortless.gui.slot.TextSlot textSlot;
    protected AbstractWidget titleTextWidget;
    protected SlotContainer slotContainer;

    public TransformerTooltipEntry(Entrance entrance, EntryList entryList, T transformer) {
        super(entrance, entryList, transformer);
    }

    protected static String formatDouble(double number) {
        var decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(number);
    }

    @Override
    public void onCreate() {
        this.textSlot = addWidget(new dev.huskuraft.effortless.gui.slot.TextSlot(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, getSymbol()));
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 2, getDisplayName()));
        this.slotContainer = addWidget(new SlotContainer(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 12, 0, 0));
    }

    @Override
    public void onReload() {
        slotContainer.setWrapLines(getEntryList().getSelected() == this);
    }

    @Override
    public void onBindItem() {
        textSlot.setMessage(getSymbol());
        slotContainer.setEntries(getInfo());
    }

    @Override
    public Text getNarration() {
        return Text.translate("narrator.select", getDisplayName());
    }


    protected List<SlotData> getInfo() {
        return Collections.emptyList();
    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return Dimens.ICON_HEIGHT + 4;
    }

    protected Text getDisplayName() {
        if (getItem().getName().isBlank()) {
            return Text.translate("effortless.transformer.no_name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
        }
        return getItem().getName();
    }

    protected Text getSymbol() {
        return getSymbol(getItem());
    }

    public static Text getSymbol(Transformer transformer) {
        return switch (transformer.getType()) {
            case ARRAY -> Text.text("AT");
            case MIRROR -> Text.text("MT");
            case RADIAL -> Text.text("RT");
            case ITEM_RAND -> Text.text("IR");
        };
    }

}
