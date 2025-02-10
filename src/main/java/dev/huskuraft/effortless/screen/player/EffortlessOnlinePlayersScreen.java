package dev.huskuraft.effortless.screen.player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.PlayerInfo;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.input.EditBox;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.ClientEntrance;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.platform.SearchTree;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessOnlinePlayersScreen extends AbstractPanelScreen {

    private final List<PlayerInfo> players;
    private final Consumer<PlayerInfo> consumer;
    private TextWidget titleTextWidget;
    private PlayerInfoList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessOnlinePlayersScreen(Entrance entrance, Consumer<PlayerInfo> consumer) {
        super(entrance, Text.translate("effortless.online_players.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.players = getEntrance().getClient().getOnlinePlayers();
        this.consumer = consumer;
    }

    @Override
    protected ClientEntrance getEntrance() {
        return super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, PANEL_TITLE_HEIGHT_2 - Button.COMPAT_SPACING_V, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.online_players.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.select"), button -> {
            detach();
            consumer.accept(entries.getSelected().getItem());
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new PlayerInfoList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_TITLE_HEIGHT_2, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_TITLE_HEIGHT_2 - PANEL_BUTTON_ROW_HEIGHT_1, true));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(players);
    }

    @Override
    public void onReload() {

        addButton.setActive(entries.hasSelected());
        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            detach();
            consumer.accept(entries.getSelected().getItem());
        }
    }

    private void setSearchResult(String string) {
        entries.reset(SearchTree.of(players, p -> Stream.of(p.getName())).search(string));
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}
