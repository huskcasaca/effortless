package dev.huskuraft.effortless.screen.general;

import java.util.Optional;
import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.screen.item.EffortlessItemsScreen;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.ConstraintConfig;

public class EffortlessGlobalGeneralSettingsScreen extends AbstractPanelScreen {

    private final Consumer<ConstraintConfig> consumer;
    private ConstraintConfig defaultConfig;
    private ConstraintConfig originalConfig;
    private ConstraintConfig config;
    private AbstractWidget playerButton;
    private AbstractWidget cancelButton;
    private AbstractWidget resetButton;
    private AbstractWidget saveButton;

    public EffortlessGlobalGeneralSettingsScreen(Entrance entrance, ConstraintConfig config, Consumer<ConstraintConfig> consumer) {
        super(entrance, Text.translate("effortless.global_general_settings.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.defaultConfig = ConstraintConfig.DEFAULT;
        this.originalConfig = config;
        this.config = config;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2, false, false));
        entries.setAlwaysShowScrollbar(true);
        //        entries.addSwitchEntry(Text.translate("effortless.general_settings.use_commands"), null, config.useCommands(), (value) -> {
//            this.config = new ConstraintConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBoxSideLengthPerBreak(), config.maxBoxSideLengthPerPlace(), config.whitelistedItems(), config.blacklistedItems());
//        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.allow_use_mod"), null, config.allowUseMod(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.allow_break_blocks"), null, config.allowBreakBlocks(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.allow_place_blocks"), null, config.allowPlaceBlocks(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.allow_interact_blocks"), null, config.allowInteractBlocks(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.allow_copy_paste_structures"), null, config.allowCopyPasteStructures(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), value, config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addSwitchEntry(Text.translate("effortless.general_settings.use_proper_tools"), null, config.useProperToolsOnly(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), value, config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.general_settings.max_reach_distance"), null, config.maxReachDistance(), ConstraintConfig.MAX_REACH_DISTANCE_RANGE_START, ConstraintConfig.MAX_REACH_DISTANCE_RANGE_END, 8, (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), value, config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.general_settings.max_block_break_volume"), null, config.maxBlockBreakVolume(), ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_BREAK_VOLUME_RANGE_END, 100, (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), value, config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.general_settings.max_block_place_volume"), null, config.maxBlockPlaceVolume(), ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_PLACE_VOLUME_RANGE_END, 100, (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), value, config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.general_settings.max_block_interact_volume"), null, config.maxBlockInteractVolume(), ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_START, ConstraintConfig.MAX_BLOCK_INTERACT_VOLUME_RANGE_END, 100, (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), value, config. maxStructureCopyPasteVolume(), config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addIntegerEntry(Text.translate("effortless.general_settings.max_structure_copy_paste_volume"), null, config.maxStructureCopyPasteVolume(), ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_START, ConstraintConfig.MAX_STRUCTURE_COPY_PASTE_VOLUME_RANGE_END, 100, (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), value, config.whitelistedItems(), config.blacklistedItems());
        });
        entries.addTab(Text.translate("effortless.general_settings.whitelisted_items"), null, config.whitelistedItems(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), value, config.blacklistedItems());
        }, (entry, value) -> {
            entry.getButton().setOnPressListener(button1 -> {
                new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.general_settings.whitelisted_items"), value.stream().map(Item::fromIdOptional).filter(Optional::isPresent).map(Optional::get).toList(), (value1) -> {
                    entry.setItem(value1.stream().map(Item::getId).distinct().toList());
                }).attach();
            });
            entry.getButton().setMessage(Text.translate("effortless.general_settings.items", value.size()));
        });
        entries.addTab(Text.translate("effortless.general_settings.blacklisted_items"), null, config.blacklistedItems(), (value) -> {
            this.config = new ConstraintConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.allowCopyPasteStructures(), config.useProperToolsOnly(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBlockInteractVolume(), config.maxStructureCopyPasteVolume(), config.whitelistedItems(), value);
        }, (entry, value) -> {
            entry.getButton().setOnPressListener(button1 -> {
                new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.general_settings.blacklisted_items"), value.stream().map(Item::fromIdOptional).filter(Optional::isPresent).map(Optional::get).toList(), (value1) -> {
                    entry.setItem(value1.stream().map(Item::getId).distinct().toList());
                }).attach();
            });
            entry.getButton().setMessage(Text.translate("effortless.general_settings.items", value.size()));
        });

        this.playerButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.per_player_overrides"), button -> {
            new EffortlessPlayerGeneralSettingsListScreen(getEntrance(), getEntrance().getSessionManager().getServerSessionConfigOrEmpty().playerConfigs(), playerConfigs -> {
                getEntrance().getSessionManager().updatePlayerConfig(playerConfigs);
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
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
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onReload() {
        this.resetButton.setActive(!config.equals(defaultConfig));
    }
}
