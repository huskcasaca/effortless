package dev.huskuraft.effortless.screen.player;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.SearchTree;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessOnlinePlayersScreen extends AbstractScreen {

    private final List<PlayerInfo> players;
    private final Consumer<PlayerInfo> consumer;
    private TextWidget titleTextWidget;
    private PlayerInfoList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessOnlinePlayersScreen(Entrance entrance, Consumer<PlayerInfo> consumer) {
        super(entrance, Text.translate("effortless.online_players.title"));
        this.players = getEntrance().getClient().getOnlinePlayers();
        this.consumer = consumer;
    }

    @Override
    protected ClientEntrance getEntrance() {
        return super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2, Dimens.Title.CONTAINER_24, Dimens.Entry.ROW_WIDTH, 20, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.online_players.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.select"), button -> {
            consumer.accept(entries.getSelected().getItem());
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new PlayerInfoList(getEntrance(), 0, Dimens.Title.CONTAINER_24 + 26, getWidth(), getHeight() - Dimens.Title.CONTAINER_24 - 26 - 36));

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
