package dev.huskuraft.effortless.screen.general;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.settings.SettingButtonsList;
import dev.huskuraft.effortless.session.config.BuildingConfig;

public class EffortlessGeneralSettingsScreen extends AbstractScreen {

    public EffortlessGeneralSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.general_settings.title"));
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, 35 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingButtonsList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 36));
        entries.addTab(Text.translate("effortless.global_general_settings.title"), (button) -> {
            new EffortlessGlobalGeneralSettingsScreen(getEntrance(), BuildingConfig.DEFAULT, config -> {

            }).attach();
        });
        entries.addTab(Text.translate("effortless.per_player_general_settings.title"), (button) -> {
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.15f, 0.7f).build());

    }

}
