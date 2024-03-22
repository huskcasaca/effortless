package dev.huskuraft.effortless.screen.general;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessPerPlayerGeneralSettingsScreen extends AbstractScreen {

    private final PlayerInfo playerInfo;
    private final GeneralConfig globalConfig;
    private GeneralConfig playerConfig;
    private final BiConsumer<PlayerInfo, GeneralConfig> consumer;


    public EffortlessPerPlayerGeneralSettingsScreen(Entrance entrance, PlayerInfo playerInfo, GeneralConfig globalConfig, GeneralConfig playerConfig, BiConsumer<PlayerInfo, GeneralConfig> consumer) {
        super(entrance, Text.translate("effortless.general_settings.title"));
        this.playerInfo = playerInfo;
        this.globalConfig = globalConfig;
        this.playerConfig = playerConfig;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        var titleWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));
        var playerNameWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, Text.text(playerInfo.getName()), TextWidget.Gravity.CENTER));

        playerNameWidget.setColor(0xFFAAAAAA);

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36, false, true));
        entries.setRenderSelection(false);
        applyEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, null, null),
                value -> {
                    this.playerConfig = new GeneralConfig(value, playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.useCommands(),
                () -> playerConfig.useCommands()
        );

        applyEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, null, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), value, playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.allowUseMod(),
                () -> playerConfig.allowUseMod()
        );
        applyEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, null, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), value, playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.allowBreakBlocks(),
                () -> playerConfig.allowBreakBlocks()
        );
        applyEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, null, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), value, playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.allowPlaceBlocks(),
                () -> playerConfig.allowPlaceBlocks()
        );
        applyEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, null, GeneralConfig.MAX_REACH_DISTANCE_RANGE_START, GeneralConfig.MAX_REACH_DISTANCE_RANGE_END, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), value, playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.maxReachDistance(),
                () -> playerConfig.maxReachDistance()
        );
        applyEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_distance_per_axis"), null, null, GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_START, GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_END, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), value, playerConfig.maxBreakBlocks(), playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.maxDistancePerAxis(),
                () -> playerConfig.maxDistancePerAxis()
        );
        applyEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_break_blocks"), null, null, GeneralConfig.MAX_BREAK_BLOCKS_RANGE_START, GeneralConfig.MAX_BREAK_BLOCKS_RANGE_END, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), value, playerConfig.maxPlaceBlocks(), playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.maxBreakBlocks(),
                () -> playerConfig.maxBreakBlocks()
        );
        applyEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_place_blocks"), null, null, GeneralConfig.MAX_PLACE_BLOCKS_RANGE_START, GeneralConfig.MAX_PLACE_BLOCKS_RANGE_END, null),
                (value) -> {
                    this.playerConfig = new GeneralConfig(playerConfig.useCommands(), playerConfig.allowUseMod(), playerConfig.allowBreakBlocks(), playerConfig.allowPlaceBlocks(), playerConfig.maxReachDistance(), playerConfig.maxDistancePerAxis(), playerConfig.maxBreakBlocks(), value, playerConfig.whitelistedItems(), playerConfig.blacklistedItems());
                },
                () -> globalConfig.maxPlaceBlocks(),
                () -> playerConfig.maxPlaceBlocks()
        );

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(playerInfo, playerConfig);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    private void applyEntryStatus(SettingOptionsList.SettingsEntry<?> entry, boolean global) {
        entry.setActive(!global);
        entry.getButton().setMessage(global ? "O" : "X");
        entry.getButton().setTooltip(
                global ? "Click to Override" : "Click to Use Global",
                ""
//                ,
//                global ? "This config value overrides the value in the global settings" : "This config value follows the value in the global settings"
        );
    }

    private <T> void applyEntry(SettingOptionsList.SettingsEntry<T> entry, Consumer<T> setter, Supplier<T> globalGetter, Supplier<T> playerGetter) {
        entry.setItem(playerGetter.get() == null ? globalGetter.get() : playerGetter.get());
        applyEntryStatus(entry, playerGetter.get() == null);

        entry.getButton().setOnPressListener(button -> {
            if (playerGetter.get() == null) {
                entry.setItem(globalGetter.get());
            } else {
                entry.setItem(globalGetter.get());
                setter.accept(null);
            }
            applyEntryStatus(entry, playerGetter.get() == null);
        });

        entry.setConsumer(setter);
    }

}
