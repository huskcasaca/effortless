package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.preview.EffortlessPreviewSettingsScreen;

public class EffortlessSettingsScreen extends AbstractScreen {

    public EffortlessSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.settings.title"));
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingButtonsList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36));
        entries.addTab(Text.translate("effortless.general_settings.title"), (button) -> {
            new EffortlessGeneralSettingsScreen(getEntrance()).attach();
        });
        entries.addTab(Text.translate("effortless.preview_settings.title"), (button) -> {
            new EffortlessPreviewSettingsScreen(getEntrance()).attach();
        });
        entries.addTab(Text.translate("effortless.pattern_settings.title"), (button) -> {
            new EffortlessPatternSettingsScreen(getEntrance()).attach();
        });
        entries.addTab(Text.translate("effortless.transformer_presets.title"), (button) -> {
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.15f, 0.7f).build());

    }

}