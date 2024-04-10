package dev.huskuraft.effortless.screen.general;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.OfflinePlayerInfo;
import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.player.PlayerInfoList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessPerPlayerGeneralSettingsListScreen extends AbstractScreen {

    private final Consumer<Map<UUID, GeneralConfig>> consumer;
    private Map<UUID, GeneralConfig> originalData;
    private Map<UUID, GeneralConfig> data;
    private PlayerInfoList entries;
    private Button deleteButton;
    private Button editButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;

    public EffortlessPerPlayerGeneralSettingsListScreen(Entrance entrance, Map<UUID, GeneralConfig> data, Consumer<Map<UUID, GeneralConfig>> editConsumer) {
        super(entrance, Text.translate("effortless.per_player_general_settings.title"));
        this.originalData = new LinkedHashMap<>(data);
        this.data = new LinkedHashMap<>(data);
        this.consumer = editConsumer;
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        var titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
                onReload();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0f, 1 / 3f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                new EffortlessPerPlayerGeneralSettingsScreen(getEntrance(), entries.getSelected().getItem(), data.getOrDefault(entries.getSelected().getItem().getId(), GeneralConfig.NULL), (playerInfo1, config) -> {
                    this.entries.insertSelected(playerInfo1);
                    this.data.put(playerInfo1.getId(), config);
                    onReload();
                }).attach();

            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 1 / 3f, 1 / 3f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessOnlinePlayersScreen(
                    getEntrance(),
                    playerInfo -> {
                        new EffortlessPerPlayerGeneralSettingsScreen(getEntrance(), playerInfo, data.getOrDefault(playerInfo.getId(), GeneralConfig.NULL), (playerInfo1, config) -> {
                            this.entries.insertSelected(playerInfo1);
                            this.data.put(playerInfo1.getId(), config);
                            onReload();
                        }).attach();
                    }
            ).attach();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 2 / 3f, 1 / 3f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(data);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new PlayerInfoList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_2, true));
        this.entries.reset(getConfigurablePlayers());
    }

    @Override
    public void onReload() {
        this.deleteButton.setActive(entries.hasSelected());
        this.editButton.setActive(entries.hasSelected());
        this.saveButton.setActive(!data.equals(originalData));
        this.data = this.entries.items().stream().map(PlayerInfo::getId).collect(Collectors.toMap(Function.identity(), data::get, (x, y) -> y, LinkedHashMap::new));
    }

    public List<PlayerInfo> getConfigurablePlayers() {
        var id2Players = getEntrance().getClient().getOnlinePlayers().stream().collect(Collectors.toMap(PlayerInfo::getId, Function.identity()));
        return data.keySet().stream().map(id -> id2Players.computeIfAbsent(id, OfflinePlayerInfo::new)).collect(Collectors.toList());
    }


}
