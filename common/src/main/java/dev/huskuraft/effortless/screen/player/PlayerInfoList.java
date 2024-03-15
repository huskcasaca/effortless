package dev.huskuraft.effortless.screen.player;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.player.PlayerAvatarIcon;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public final class PlayerInfoList extends EditableEntryList<PlayerInfo> {

    public PlayerInfoList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @Override
    protected Entry createHolder(PlayerInfo item) {
        return new Entry(getEntrance(), item);
    }

    public static class Entry extends EditableEntryList.Entry<PlayerInfo> {

        private PlayerAvatarIcon icon;
        private TextWidget textWidget;

        public Entry(Entrance entrance, PlayerInfo playerInfo) {
            super(entrance, playerInfo);
        }

        @Override
        public void onCreate() {
            this.icon = addWidget(new PlayerAvatarIcon(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getItem()));
            this.textWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, Text.text(getItem().name())));
        }

        @Override
        public void onReload() {
            this.icon.setPlayerInfo(item);
            this.textWidget.setMessage(item.name());
        }

        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", getItem().displayName());
        }

        @Override
        public int getWidth() {
            return Dimens.Entry.ROW_WIDTH;
        }

        @Override
        public int getHeight() {
            return 24;
        }

    }
}
