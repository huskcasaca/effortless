package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.icon.RadialTextIcon;
import dev.huskuraft.effortless.gui.icon.TextIcon;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.transformer.info.TransformerInfoEntry;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.List;

public class PatternEntry extends EditableEntry<Pattern> {

    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;
    private static final int MAX_SLOT_COUNT = Dimens.RegularEntry.MAX_SLOT_COUNT;

    private RadialTextIcon radialTextIcon;
    private TextWidget nameTextWidget;
    private List<AbstractWidget> slots;

    protected PatternEntry(Entrance entrance, Pattern pattern) {
        super(entrance, pattern);
    }

    @Override
    public void onCreate() {
        this.radialTextIcon = addWidget(new RadialTextIcon(getEntrance(), getX() + 1, getY() + 1, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, 0, Text.text(String.valueOf(0))));
        this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 3, getY() + 2, getDisplayName(getItem())));
        updateSlots();
    }

    @Override
    public void onPositionChange(int from, int to) {
        radialTextIcon.setIndex(to);
        radialTextIcon.setMessage(Text.text(String.valueOf(to + 1)));
    }

    @Override
    public void setItem(Pattern item) {
        super.setItem(item);
        nameTextWidget.setMessage(getDisplayName(getItem()));
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
        return 24 + 12;
    }

    private void updateSlots() {
        var slots = new ArrayList<AbstractWidget>();
        var slot = 0;
        for (var transformer : getItem().transformers()) {
            slots.add(addWidget(new TextIcon(getEntrance(), getX() + slot * Dimens.SLOT_OFFSET_X + Dimens.ICON_WIDTH + 3, getY() + 13, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, TransformerInfoEntry.getSymbol(transformer))));
            if (slot++ == MAX_SLOT_COUNT) {
                break;
            }
        }
        this.slots = slots;
    }

    private Text getDisplayName(Pattern pattern) {
        if (pattern.name().getString().isBlank()) {
            return Text.text("No Name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
        }
        return pattern.name();
    }


}
