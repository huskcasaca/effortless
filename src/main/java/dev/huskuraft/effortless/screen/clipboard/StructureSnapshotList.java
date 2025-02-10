package dev.huskuraft.effortless.screen.clipboard;

import dev.huskuraft.universal.api.gui.Dimens;
import dev.huskuraft.universal.api.gui.container.AbstractEntryList;
import dev.huskuraft.universal.api.gui.container.EditableEntryList;
import dev.huskuraft.universal.api.gui.slot.ItemSlot;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

import java.util.UUID;

public final class StructureSnapshotList extends EditableEntryList<Snapshot> {

    public StructureSnapshotList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected SnapshotEntry createHolder(Snapshot Snapshot) {
        return new SnapshotEntry(getEntrance(), Snapshot);
    }

    private static class SnapshotEntry extends Entry<Snapshot> {

        private StructureSnapshotWidget snapshotWidget;
        private TextWidget textWidget;
        private TextWidget uuidTextWidget;

        public SnapshotEntry(Entrance entrance, Snapshot snapshot) {
            super(entrance, snapshot);
        }

        @Override
        public void onCreate() {
            this.snapshotWidget = addWidget(new StructureSnapshotWidget(getEntrance(), getX() + 1, getY() + 1, Dimens.Icon.SIZE_66, Dimens.Icon.SIZE_66, getItem()));
            this.snapshotWidget.setBackgroundColor(0x9f6c6c6c);
            this.textWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_66, getY() + 4, Text.text("Structure Snapshot")));
            this.uuidTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_66, getY() + 4 + 11, Text.text(UUID.randomUUID().toString()).withStyle(ChatFormatting.GRAY)));

            int xOffset = 0;
            int yOffset = 0;
            for (var itemStack : getItem().getItems()) {
                addWidget(new ItemSlot(getEntrance(), getX() + 5 + Dimens.Icon.SIZE_66 + xOffset, getY() + 4 + 11 + 11 + yOffset, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, itemStack, Text.text(String.valueOf(itemStack.getCount()))));
                xOffset += Dimens.SLOT_WIDTH + 2;
                if (xOffset > getWidth() - Dimens.Icon.SIZE_66 - 5 - 20) {
                    xOffset = 0;
                    yOffset += Dimens.SLOT_HEIGHT + 2;
                }
//                if (yOffset > getHeight() - 4 - 11 -11 && !isFocused()) {
//                    break;
//                }
            }
        }

        @Override
        public void onReload() {

        }

        @Override
        public int getHeight() {
            if (((AbstractEntryList) getParent()).getSelected() == this) {
                return Dimens.Icon.SIZE_66 + 4 + Math.max((getItem().getItems().size() + 8) / 9 - 2, 0) * (Dimens.SLOT_HEIGHT + 2);
            }
            return Dimens.Icon.SIZE_66 + 6;
        }

    }
}
