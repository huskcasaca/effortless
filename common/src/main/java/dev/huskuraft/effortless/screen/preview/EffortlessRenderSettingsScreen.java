package dev.huskuraft.effortless.screen.preview;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.RenderSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessRenderSettingsScreen extends AbstractScreen {

    private final Consumer<RenderSettings> consumer;
    private RenderSettings originalConfig;
    private RenderSettings lastConfig;

    private AbstractWidget saveButton;

    public EffortlessRenderSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.render_settings.title"));
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
            getEntrance().getTagConfigStorage().update(config -> new RootSettings(this.lastConfig, config.patternSettings(), config.transformerPresets()));
        };
        this.lastConfig = getEntrance().getTagConfigStorage().get().renderSettings();
        this.originalConfig = lastConfig;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36, false, false));
        entries.addSwitchEntry(Text.translate("effortless.render_settings.show_other_players_build"), null, lastConfig.showOtherPlayersBuild(), (value) -> {
            this.lastConfig = new RenderSettings(value, lastConfig.showBlockPreview(), lastConfig.maxRenderDistance(), lastConfig.maxRenderBlocks());
        });
        entries.addSwitchEntry(Text.translate("effortless.render_settings.show_block_preview"), null, lastConfig.showBlockPreview(), (value) -> {
            this.lastConfig = new RenderSettings(lastConfig.showOtherPlayersBuild(), value, lastConfig.maxRenderDistance(), lastConfig.maxRenderBlocks());
        });

        entries.addIntegerEntry(Text.translate("effortless.render_settings.max_render_blocks"), null, lastConfig.maxRenderBlocks(), RenderSettings.MIN_MAX_RENDER_BLOCKS, RenderSettings.MAX_MAX_RENDER_BLOCKS, (value) -> {
            this.lastConfig = new RenderSettings(lastConfig.showOtherPlayersBuild(), lastConfig.showBlockPreview(), value, lastConfig.maxRenderDistance());
        });
        entries.addIntegerEntry(Text.translate("effortless.render_settings.max_render_distance"), null, lastConfig.maxRenderDistance(), RenderSettings.MIN_MAX_RENDER_DISTANCE, RenderSettings.MAX_MAX_RENDER_DISTANCE, (value) -> {
            this.lastConfig = new RenderSettings(lastConfig.showOtherPlayersBuild(), lastConfig.showBlockPreview(), lastConfig.maxRenderBlocks(), value);
        });


        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(lastConfig);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    @Override
    public void onReload() {
        this.saveButton.setActive(!originalConfig.equals(lastConfig));
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
