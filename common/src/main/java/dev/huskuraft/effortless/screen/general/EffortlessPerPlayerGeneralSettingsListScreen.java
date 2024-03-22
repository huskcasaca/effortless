package dev.huskuraft.effortless.screen.general;

import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.player.PlayerInfoList;

public class EffortlessPerPlayerGeneralSettingsListScreen extends AbstractScreen {

    private final Consumer<PlayerInfo> consumer;
    private List<PlayerInfo> data;
    private TextWidget titleTextWidget;
    private PlayerInfoList entries;
    private Button editButton;
    private Button deleteButton;
    private Button duplicateButton;
    private Button addButton;
    private Button doneButton;
    private Button cancelButton;

    public EffortlessPerPlayerGeneralSettingsListScreen(Entrance entrance, List<PlayerInfo> data, Consumer<PlayerInfo> editConsumer) {
        super(entrance, Text.translate("effortless.online_players.title"));
        this.data = data;
        this.consumer = editConsumer;
    }

    @Override
    protected ClientEntrance getEntrance() {
        return super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.f, 0.25f).build());
        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.duplicate"), button -> {
            if (entries.hasSelected()) {
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                consumer.accept(entries.getSelected().getItem());
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessOnlinePlayersScreen(
                    getEntrance(),
                    playerInfo -> {
                        this.entries.insertSelected(playerInfo);
                        consumer.accept(playerInfo);
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new PlayerInfoList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 60));
        this.entries.reset(data);
    }

    @Override
    public void onReload() {
        this.data = this.entries.items();
    }

}
