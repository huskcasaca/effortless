package dev.huskuraft.effortless.screen.render;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessRenderSettingsScreen extends AbstractPanelScreen {

    private final Consumer<RenderConfig> consumer;
    private RenderConfig originalConfig;
    private RenderConfig config;

    private AbstractWidget saveButton;

    public EffortlessRenderSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.render_settings.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
            getEntrance().getConfigStorage().update(config -> config.withRenderConfig(this.config));
        };
        this.config = getEntrance().getConfigStorage().get().renderConfig();
        this.originalConfig = config;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1, false, false));
        entries.addSwitchEntry(Text.translate("effortless.render_settings.show_block_preview"), null, config.showBlockPreview(), (value) -> {
            this.config = new RenderConfig(value, config.showOtherPlayersBuild(), config.showOtherPlayersBuildTooltips(), config.maxRenderVolume(), config.maxRenderDistance());
        });
        entries.addSwitchEntry(Text.translate("effortless.render_settings.show_other_players_build"), null, config.showOtherPlayersBuild(), (value) -> {
            this.config = new RenderConfig(config.showBlockPreview(), value, config.showOtherPlayersBuildTooltips(), config.maxRenderVolume(), config.maxRenderDistance());
        });
//        entries.addSwitchEntry(Text.translate("effortless.render_settings.show_other_players_build_tooltips"), null, config.showOtherPlayersBuildTooltips(), (value) -> {
//            this.config = new RenderConfig(config.showBlockPreview(), config.showOtherPlayersBuild(), value, config.maxRenderVolume(), config.maxRenderDistance());
//        });
        entries.addIntegerEntry(Text.translate("effortless.render_settings.max_render_volume"), null, config.maxRenderVolume(), RenderConfig.MAX_RENDER_VOLUME_MIN, RenderConfig.MAX_RENDER_VOLUME_MAX, (value) -> {
            this.config = new RenderConfig(config.showBlockPreview(), config.showOtherPlayersBuild(), config.showOtherPlayersBuildTooltips(), value, config.maxRenderDistance());
        });
//        entries.addIntegerEntry(Text.translate("effortless.render_settings.max_render_distance"), null, config.maxRenderDistance(), RenderConfig.MIN_MAX_RENDER_DISTANCE, RenderConfig.MAX_MAX_RENDER_DISTANCE, (value) -> {
//            this.config = new RenderConfig(config.showOtherPlayersBuild(), config.showOtherPlayersBuildTooltips(), config.showBlockPreview(), config.maxRenderVolume(), value);
//        });


        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    @Override
    public void onReload() {
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
