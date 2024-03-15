package dev.huskuraft.effortless.screen.general;

import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.BuildingConfig;

public class EffortlessGlobalGeneralSettingsScreen extends AbstractScreen {

    private BuildingConfig config;
    private final Consumer<BuildingConfig> consumer;


    public EffortlessGlobalGeneralSettingsScreen(Entrance entrance, BuildingConfig config, Consumer<BuildingConfig> consumer) {
        super(entrance, Text.translate("effortless.general_settings.title"));
        this.config = config;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36, false, false));
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, config.useCommands(), (value) -> {
            this.config = new BuildingConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, config.allowUseMod(), (value) -> {
            this.config = new BuildingConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, config.allowBreakBlocks(), (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, config.allowPlaceBlocks(), (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, config.maxReachDistance(), BuildingConfig.MAX_REACH_DISTANCE_RANGE_START, BuildingConfig.MAX_REACH_DISTANCE_RANGE_END, (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.maxDistancePerAxis(), config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_distance_per_axis"), null, config.maxDistancePerAxis(), BuildingConfig.MAX_DISTANCE_PER_AXIS_RANGE_START, BuildingConfig.MAX_DISTANCE_PER_AXIS_RANGE_END, (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), value, config.maxBreakBlocks(), config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_break_blocks"), null, config.maxBreakBlocks(), BuildingConfig.MAX_BREAK_BLOCKS_RANGE_START, BuildingConfig.MAX_BREAK_BLOCKS_RANGE_END, (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), value, config.maxPlaceBlocks(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_place_blocks"), null, config.maxPlaceBlocks(), BuildingConfig.MAX_PLACE_BLOCKS_RANGE_START, BuildingConfig.MAX_PLACE_BLOCKS_RANGE_END, (value) -> {
            this.config = new BuildingConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxDistancePerAxis(), config.maxBreakBlocks(), value, config.whitelistedItems(), config.blacklistedItems());
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
