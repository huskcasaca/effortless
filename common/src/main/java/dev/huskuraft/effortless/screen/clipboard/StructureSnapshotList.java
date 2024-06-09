package dev.huskuraft.effortless.screen.clipboard;

import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
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

        private StructureSnapshotWidget icon;
        private TextWidget textWidget;
        private TextWidget uuidTextWidget;

        public SnapshotEntry(Entrance entrance, Snapshot snapshot) {
            super(entrance, snapshot);
        }

        @Override
        public void onCreate() {
            this.icon = addWidget(new StructureSnapshotWidget(getEntrance(), getX() + 1, getY() + 1, Dimens.Icon.SIZE_62, Dimens.Icon.SIZE_62, getItem()));
            this.textWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_62, getY() + 4, Text.text("Structure Snapshot")));
            this.uuidTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_62, getY() + 4 + 11, Text.text(UUID.randomUUID().toString()).withStyle(ChatFormatting.GRAY)));
        }

        @Override
        public void onReload() {

        }

        @Override
        public int getHeight() {
            return Dimens.Icon.SIZE_62 + 6;
        }

    }
}
