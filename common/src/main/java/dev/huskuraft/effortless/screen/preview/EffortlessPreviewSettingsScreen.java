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
import dev.huskuraft.effortless.building.config.PreviewSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessPreviewSettingsScreen extends AbstractScreen {

    private final Consumer<PreviewSettings> consumer;
    private PreviewSettings originalConfig;
    private PreviewSettings lastConfig;

    private AbstractWidget saveButton;

    public EffortlessPreviewSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.preview_settings.title"));
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
            getEntrance().getConfigStorage().update(config -> new RootSettings(this.lastConfig, config.patternSettings(), config.transformerPresets()));
        };
        this.lastConfig = getEntrance().getConfigStorage().get().previewSettings();
        this.originalConfig = lastConfig;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36, false, false));
        entries.addSwitchEntry(Text.translate("effortless.preview_settings.use_commands"), null, lastConfig.alwaysShowBlockPreview(), (value) -> {
            this.lastConfig = new PreviewSettings(lastConfig.itemUsagePosition(), lastConfig.buildInfoPosition(), value, lastConfig.shaderDissolveTimeMultiplier());
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
