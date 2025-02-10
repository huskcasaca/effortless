package dev.huskuraft.effortless.screen.general;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.OfflinePlayerInfo;
import dev.huskuraft.universal.api.core.PlayerInfo;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.player.PlayerInfoList;
import dev.huskuraft.effortless.session.config.ConstraintConfig;

public class EffortlessPlayerGeneralSettingsListScreen extends AbstractPanelScreen {

    private final Consumer<Map<UUID, ConstraintConfig>> consumer;
    private Map<UUID, ConstraintConfig> defaultConfig;
    private Map<UUID, ConstraintConfig> originalConfig;
    private Map<UUID, ConstraintConfig> config;
    private PlayerInfoList entries;
    private Button editButton;
    private Button deleteButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;

    public EffortlessPlayerGeneralSettingsListScreen(Entrance entrance, Map<UUID, ConstraintConfig> config, Consumer<Map<UUID, ConstraintConfig>> editConsumer) {
        super(entrance, Text.translate("effortless.player_general_settings.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.defaultConfig = new LinkedHashMap<>();
        this.originalConfig = new LinkedHashMap<>(config);
        this.config = new LinkedHashMap<>(config);
        this.consumer = editConsumer;
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        var titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                new EffortlessPlayerGeneralSettingsScreen(getEntrance(), entries.getSelected().getItem(), config.getOrDefault(entries.getSelected().getItem().getId(), ConstraintConfig.NULL), (playerInfo1, config) -> {
                    this.entries.insertSelected(playerInfo1);
                    this.config.put(playerInfo1.getId(), config);
                    onReload();
                }).attach();

            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1 / 3f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
                onReload();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 3f, 1 / 3f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessOnlinePlayersScreen(
                    getEntrance(),
                    playerInfo -> {
                        new EffortlessPlayerGeneralSettingsScreen(getEntrance(), playerInfo, config.getOrDefault(playerInfo.getId(), ConstraintConfig.NULL), (playerInfo1, config) -> {
                            this.entries.insertSelected(playerInfo1);
                            this.config.put(playerInfo1.getId(), config);
                            onReload();
                        }).attach();
                    }
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2 / 3f, 1 / 3f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new PlayerInfoList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2, true));
        this.entries.reset(getConfigurablePlayers());
        this.entries.setAlwaysShowScrollbar(true);
    }

    @Override
    public void onReload() {
        this.deleteButton.setActive(entries.hasSelected());
        this.editButton.setActive(entries.hasSelected());
        this.config = this.entries.items().stream().map(PlayerInfo::getId).collect(Collectors.toMap(Function.identity(), config::get, (e1, e2) -> e1, LinkedHashMap::new));

        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            new EffortlessPlayerGeneralSettingsScreen(getEntrance(), entries.getSelected().getItem(), config.getOrDefault(entries.getSelected().getItem().getId(), ConstraintConfig.NULL), (playerInfo1, config) -> {
                this.entries.insertSelected(playerInfo1);
                this.config.put(playerInfo1.getId(), config);
                onReload();
            }).attach();
        }
    }

    public List<PlayerInfo> getConfigurablePlayers() {
        var id2Players = getEntrance().getClient().getOnlinePlayers().stream().collect(Collectors.toMap(PlayerInfo::getId, Function.identity()));
        return config.keySet().stream().map(id -> id2Players.computeIfAbsent(id, OfflinePlayerInfo::new)).collect(Collectors.toList());
    }


}
