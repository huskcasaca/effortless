package dev.huskuraft.effortless.screen.general;

import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessGlobalGeneralSettingsScreen extends AbstractScreen {

    private GeneralConfig config;
    private final Consumer<GeneralConfig> consumer;


    public EffortlessGlobalGeneralSettingsScreen(Entrance entrance, GeneralConfig config, Consumer<GeneralConfig> consumer) {
        super(entrance, Text.translate("effortless.global_general_settings.title"));
        this.config = config;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_1, false, false));
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, config.useCommands(), (value) -> {
            this.config = new GeneralConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, config.allowUseMod(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, config.allowBreakBlocks(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, config.allowPlaceBlocks(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, config.maxReachDistance(), GeneralConfig.MAX_REACH_DISTANCE_RANGE_START, GeneralConfig.MAX_REACH_DISTANCE_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_distance_per_axis"), null, config.maxDistancePerAxis(), GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_START, GeneralConfig.MAX_DISTANCE_PER_AXIS_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), value, config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_break_blocks"), null, config.maxBreakBlocks(), GeneralConfig.MAX_BREAK_BLOCKS_RANGE_START, GeneralConfig.MAX_BREAK_BLOCKS_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), value, config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_place_blocks"), null, config.maxPlaceBlocks(), GeneralConfig.MAX_PLACE_BLOCKS_RANGE_START, GeneralConfig.MAX_PLACE_BLOCKS_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), value, config.whitelistedItems(), config.blacklistedItems());
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

}
