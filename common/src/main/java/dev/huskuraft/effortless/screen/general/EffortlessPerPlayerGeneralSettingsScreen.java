package dev.huskuraft.effortless.screen.general;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessPerPlayerGeneralSettingsScreen extends AbstractScreen {

    private final PlayerInfo playerInfo;
    private GeneralConfig originalConfig;
    private GeneralConfig config;
    private GeneralConfig globalConfig;
    private final BiConsumer<PlayerInfo, GeneralConfig> consumer;
    private AbstractWidget saveButton;


    public EffortlessPerPlayerGeneralSettingsScreen(Entrance entrance, PlayerInfo playerInfo, GeneralConfig config, BiConsumer<PlayerInfo, GeneralConfig> consumer) {
        super(entrance, Text.translate("effortless.general_settings.title"));
        this.playerInfo = playerInfo;
        this.originalConfig = config;
        this.config = config;
        this.globalConfig = getEntrance().getSessionManager().getServerSessionConfigOrEmpty().getGlobalConfig();
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        var titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));
        var playerNameTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, Text.text(playerInfo.getName()), TextWidget.Gravity.CENTER));
        playerNameTextWidget.setColor(0xffaaaaaa);

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_1, false, true));
        entries.setRenderSelection(false);
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, null, null),
                value -> {
                    this.config = new GeneralConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.useCommands(),
                () -> config.useCommands()
        );

        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowUseMod(),
                () -> config.allowUseMod()
        );
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowBreakBlocks(),
                () -> config.allowBreakBlocks()
        );
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowPlaceBlocks(),
                () -> config.allowPlaceBlocks()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, null, GeneralConfig.MAX_REACH_DISTANCE_RANGE_START, GeneralConfig.MAX_REACH_DISTANCE_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxReachDistance(),
                () -> config.maxReachDistance()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_distance_per_axis"), null, null, GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_START, GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), value, config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxDistancePerAxis(),
                () -> config.maxDistancePerAxis()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_break_box_volume"), null, null, GeneralConfig.MAX_BREAK_BOX_VOLUME_RANGE_START, GeneralConfig.MAX_BREAK_BOX_VOLUME_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), value, config.maxPlaceBoxVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxBreakBoxVolume(),
                () -> config.maxBreakBoxVolume()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_place_box_volume"), null, null, GeneralConfig.MAX_PLACE_BOX_VOLUME_RANGE_START, GeneralConfig.MAX_PLACE_BOX_VOLUME_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), value, config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxPlaceBoxVolume(),
                () -> config.maxPlaceBoxVolume()
        );
//        bindEntry(
//                entries.addTab(Text.translate("effortless.global_general_settings.whitelisted_items"), null, null, null, (entry, value) -> {
//                    entry.getButton().setOnPressListener(button1 -> {
//                        new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.whitelisted_items"), value.stream().map(Item::fromId).toList(), (value1) -> {
//                            entry.setItem(value1.stream().map(Item::getId).toList());
//                        }).attach();
//                    });
//                    entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value == null ? null : value.size()));
//                }),
//                (value) -> {
//                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), value, config.blacklistedItems());
//                },
//                () -> globalConfig.whitelistedItems(),
//                () -> config.whitelistedItems()
//
//        );
//
//        bindEntry(
//                entries.addTab(Text.translate("effortless.global_general_settings.blacklisted_items"), null, null, null, (entry, value) -> {
//                    entry.getButton().setOnPressListener(button1 -> {
//                        new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.blacklisted_items"), value.stream().map(Item::fromId).toList(), (value1) -> {
//                            entry.setItem(value1.stream().map(Item::getId).toList());
//                        }).attach();
//                    });
//                    entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value == null ? null : value.size()));
//                }),
//                (value) -> {
//                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBoxVolume(), config.maxPlaceBoxVolume(), config.whitelistedItems(), value);
//                },
//                () -> globalConfig.blacklistedItems(),
//                () -> config.blacklistedItems()
//        );

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(playerInfo, config);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    @Override
    public void onReload() {
        this.saveButton.setActive(!config.equals(originalConfig));
        this.globalConfig = getEntrance().getSessionManager().getServerSessionConfigOrEmpty().getGlobalConfig();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    private void bindEntryState(SettingOptionsList.SettingsEntry<?> entry, boolean isGlobal) {
        entry.setActive(!isGlobal);
        entry.getAltButton().setMessage(isGlobal ? "O" : "X");
        entry.getAltButton().setTooltip(
                isGlobal ? "Click to Override" : "Click to Use Global",
                ""
//                ,
//                global ? "This config value overrides the value in the global settings" : "This config value follows the value in the global settings"
        );
    }

    private <T> void bindEntry(SettingOptionsList.SettingsEntry<T> entry, Consumer<T> setter, Supplier<T> globalGetter, Supplier<T> playerGetter) {
        entry.setItem(playerGetter.get() == null ? globalGetter.get() : playerGetter.get());
        bindEntryState(entry, playerGetter.get() == null);

        entry.getAltButton().setOnPressListener(button -> {
            if (playerGetter.get() == null) {
                entry.setItem(globalGetter.get());
            } else {
                entry.setItem(globalGetter.get());
                setter.accept(null);
            }
            bindEntryState(entry, playerGetter.get() == null);
        });

        entry.setConsumer(setter);
    }

}
