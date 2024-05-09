package dev.huskuraft.effortless.screen.pattern;

import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.icon.RadialTextIcon;
import dev.huskuraft.effortless.api.gui.slot.SlotContainer;
import dev.huskuraft.effortless.api.gui.slot.SlotData;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.screen.transformer.TransformerList;

public final class PatternList extends EditableEntryList<Pattern> {

    public PatternList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected Entry createHolder(Pattern item) {
        return new Entry(getEntrance(), item);
    }

    public static class Entry extends EditableEntryList.Entry<Pattern> {

        private RadialTextIcon radialTextIcon;
        private TextWidget nameTextWidget;
        private SlotContainer slotContainer;

        protected Entry(Entrance entrance, Pattern pattern) {
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
        public void onReload() {
            nameTextWidget.setMessage(getDisplayName(getItem()));
            slotContainer.setEntries(getItem().transformers().stream().map(transformer -> new SlotData.TextSymbol(TransformerList.Entry.getSymbol(transformer), Text.empty())).collect(Collectors.toList()));
        }

        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", Text.empty());
        }

        @Override
        public int getWidth() {
            return Math.min(Dimens.Entry.ROW_WIDTH, getParent().getWidth() - 8);
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
            if (pattern.name().getString().isBlank()) {
                return Text.translate("effortless.pattern.no_name").withStyle(ChatFormatting.GRAY);
            }
            return pattern.name();
        }


    }
}
