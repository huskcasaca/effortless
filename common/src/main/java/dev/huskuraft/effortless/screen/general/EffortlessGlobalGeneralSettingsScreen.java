package dev.huskuraft.effortless.screen.general;

import java.util.Optional;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.item.EffortlessItemsScreen;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessGlobalGeneralSettingsScreen extends AbstractPanelScreen {

    private final Consumer<GeneralConfig> consumer;
    private GeneralConfig defaultConfig;
    private GeneralConfig originalConfig;
    private GeneralConfig config;
    private AbstractWidget resetButton;
    private AbstractWidget saveButton;

    public EffortlessGlobalGeneralSettingsScreen(Entrance entrance, GeneralConfig config, Consumer<GeneralConfig> consumer) {
        super(entrance, Text.translate("effortless.global_general_settings.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.defaultConfig = GeneralConfig.DEFAULT;
        this.originalConfig = config;
        this.config = config;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1, false, false));
        entries.setAlwaysShowScrollbar(true);
        //        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, config.useCommands(), (value) -> {
//            this.config = new GeneralConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBoxSideLengthPerBreak(), config.maxBoxSideLengthPerPlace(), config.whitelistedItems(), config.blacklistedItems());
//        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, config.allowUseMod(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, config.allowBreakBlocks(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, config.allowPlaceBlocks(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_interact_blocks"), null, config.allowInteractBlocks(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_correct_tools"), null, config.useCorrectTools(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), value, config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, config.maxReachDistance(), GeneralConfig.MAX_REACH_DISTANCE_RANGE_START, GeneralConfig.MAX_REACH_DISTANCE_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), value, config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_block_break_volume"), null, config.maxBlockBreakVolume(), GeneralConfig.MAX_BOX_VOLUME_PER_BREAK_RANGE_START, GeneralConfig.MAX_BOX_VOLUME_PER_BREAK_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), value, config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_block_place_volume"), null, config.maxBlockPlaceVolume(), GeneralConfig.MAX_BOX_VOLUME_PER_PLACE_RANGE_START, GeneralConfig.MAX_BOX_VOLUME_PER_PLACE_RANGE_END, (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), value, config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addTab(Text.translate("effortless.global_general_settings.whitelisted_items"), null, config.whitelistedItems(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), value, config.blacklistedItems());
        }, (entry, value) -> {
            entry.getButton().setOnPressListener(button1 -> {
                new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.whitelisted_items"), value.stream().map(Item::fromIdOptional).filter(Optional::isPresent).map(Optional::get).toList(), (value1) -> {
                    entry.setItem(value1.stream().map(Item::getId).distinct().toList());
                }).attach();
            });
            entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value.size()));
        });
        entries.addTab(Text.translate("effortless.global_general_settings.blacklisted_items"), null, config.blacklistedItems(), (value) -> {
            this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.useCorrectTools(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), value);
        }, (entry, value) -> {
            entry.getButton().setOnPressListener(button1 -> {
                new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.blacklisted_items"), value.stream().map(Item::fromIdOptional).filter(Optional::isPresent).map(Optional::get).toList(), (value1) -> {
                    entry.setItem(value1.stream().map(Item::getId).distinct().toList());
                }).attach();
            });
            entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value.size()));
        });
        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1 / 3f).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.reset"), button -> {
            config = defaultConfig;
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 1 / 3f, 1 / 3f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 2 / 3f, 1 / 3f).build());


        this.resetButton.setActive(false);
    }

    @Override
    public void onReload() {
        this.resetButton.setActive(!config.equals(defaultConfig));
    }
}
