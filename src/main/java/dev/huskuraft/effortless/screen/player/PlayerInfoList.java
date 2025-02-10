package dev.huskuraft.effortless.screen.player;

import dev.huskuraft.universal.api.core.PlayerInfo;
import dev.huskuraft.universal.api.gui.Dimens;
import dev.huskuraft.universal.api.gui.container.EditableEntryList;
import dev.huskuraft.universal.api.gui.player.PlayerAvatarIcon;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;

public final class PlayerInfoList extends EditableEntryList<PlayerInfo> {

    private final boolean isLargeIcon;

    public PlayerInfoList(Entrance entrance, int x, int y, int width, int height, boolean isLargeIcon) {
        super(entrance, x, y, width, height);
        this.isLargeIcon = isLargeIcon;
    }

    @Override
    protected Entry createHolder(PlayerInfo item) {
        if (this.isLargeIcon) {
            return new LargeEntry(getEntrance(), item);
        }
        return new NormalEntry(getEntrance(), item);
    }

    private static class NormalEntry extends Entry<PlayerInfo> {

        private PlayerAvatarIcon icon;
        private TextWidget textWidget;

        public NormalEntry(Entrance entrance, PlayerInfo playerInfo) {
            super(entrance, playerInfo);
        }

        @Override
        public void onCreate() {
            this.icon = addWidget(new PlayerAvatarIcon(getEntrance(), getX() + 1, getY() + 1, Dimens.Icon.SIZE_18, getItem()));
            this.textWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_18, getY() + 6, Text.text(getItem().getName())));
        }

        @Override
        public void onReload() {
            this.icon.setPlayerInfo(item);
            this.textWidget.setMessage(item.getName());
        }

        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", getItem().getDisplayName());
        }

        @Override
        public int getHeight() {
            return Dimens.Icon.SIZE_18 + 6;
        }

    }

    private static class LargeEntry extends Entry<PlayerInfo> {

        private PlayerAvatarIcon icon;
        private TextWidget textWidget;
        private TextWidget uuidTextWidget;

        public LargeEntry(Entrance entrance, PlayerInfo playerInfo) {
            super(entrance, playerInfo);
        }

        @Override
        public void onCreate() {
            this.icon = addWidget(new PlayerAvatarIcon(getEntrance(), getX() + 1, getY() + 1, Dimens.Icon.SIZE_26, getItem()));
            this.textWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_26, getY() + 4, Text.text(getItem().getName())));
            this.uuidTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 6 + Dimens.Icon.SIZE_26, getY() + 4 + 11, Text.text(getItem().getId().toString()).withStyle(ChatFormatting.GRAY)));
        }

        @Override
        public void onReload() {
            this.icon.setPlayerInfo(item);
            this.textWidget.setMessage(item.getName());
        }

        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", getItem().getDisplayName());
        }

        @Override
        public int getHeight() {
            return Dimens.Icon.SIZE_26 + 6;
        }

    }
}
