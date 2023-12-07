package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.icon.RadialTextIcon;
import dev.huskuraft.effortless.gui.slot.SlotContainer;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.transformer.tooltip.TransformerTooltipEntry;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.stream.Collectors;

public class PatternEntry extends EditableEntry<Pattern> {

    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;
    private static final int MAX_SLOT_COUNT = Dimens.RegularEntry.MAX_SLOT_COUNT;

    private RadialTextIcon radialTextIcon;
    private TextWidget nameTextWidget;
    private SlotContainer slotContainer;

    protected PatternEntry(Entrance entrance, Pattern pattern) {
        super(entrance, pattern);
    }

    @Override
    public void onCreate() {
        this.radialTextIcon = addWidget(new RadialTextIcon(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, 0, Text.empty()));
        this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 2, getDisplayName(getItem())));
        this.slotContainer = addWidget(new SlotContainer(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 12, 0, 0));
    }

    @Override
    public void onPositionChange(int from, int to) {
        radialTextIcon.setIndex(to);
        radialTextIcon.setMessage(Text.text(String.valueOf(to + 1)));
    }

    @Override
    public void onBindItem() {
        nameTextWidget.setMessage(getDisplayName(getItem()));
        slotContainer.setEntries(getItem().transformers().stream().map(transformer -> new SlotData.TextSymbol(TransformerTooltipEntry.getSymbol(transformer), Text.empty())).collect(Collectors.toList()));
    }

    @Override
    public Text getNarration() {
        return Text.translate("narrator.select", Text.empty());
    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return Dimens.ICON_HEIGHT + 4;
    }

    private void updateSlots() {
//        this.slots.clear();
//        var index = 0;
//        for (var transformer : getItem().transformers()) {
//            slots.add(addWidget(new TextSlot(getEntrance(), getX() + index * Dimens.SLOT_OFFSET_X + Dimens.ICON_WIDTH + 3, getY() + 13, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, TransformerInfoEntry.getSymbol(transformer))));
//            if (index++ == MAX_SLOT_COUNT) {
//                break;
//            }
//        }
    }

    private Text getDisplayName(Pattern pattern) {
        if (pattern.name().isBlank()) {
            return Text.translate("effortless.pattern.no_name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
        }
        return pattern.name();
    }


}
