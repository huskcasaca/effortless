package dev.huskuraft.effortless.screen.player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.SearchTree;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessOnlinePlayersScreen extends AbstractContainerScreen {

    private final List<PlayerInfo> players;
    private final Consumer<PlayerInfo> consumer;
    private TextWidget titleTextWidget;
    private PlayerInfoList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessOnlinePlayersScreen(Entrance entrance, Consumer<PlayerInfo> consumer) {
        super(entrance, Text.translate("effortless.online_players.title"), CONTAINER_WIDTH_EXPANDED, CONTAINER_HEIGHT_270);
        this.players = getEntrance().getClient().getOnlinePlayers();
        this.consumer = consumer;
    }

    @Override
    protected ClientEntrance getEntrance() {
        return super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + TITLE_CONTAINER - 10, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getLeft() + PADDINGS, getTop() + TITLE_CONTAINER, getWidth() - PADDINGS * 2, TITLE_CONTAINER_2 - Button.COMPAT_SPACING_V, Text.translate("effortless.item.picker.search"))
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

        this.entries = addWidget(new PlayerInfoList(getEntrance(), getLeft() + PADDINGS, getTop() + TITLE_CONTAINER + TITLE_CONTAINER_2, getWidth() - PADDINGS * 2 - 8, getHeight() - TITLE_CONTAINER - TITLE_CONTAINER_2 - BUTTON_CONTAINER_ROW_1, true));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(players);
    }

    @Override
    public void onReload() {
        super.onReload();

        addButton.setActive(entries.hasSelected());
    }

    private void setSearchResult(String string) {
        entries.reset(SearchTree.of(players, p -> Stream.of(p.getName())).search(string));
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}
