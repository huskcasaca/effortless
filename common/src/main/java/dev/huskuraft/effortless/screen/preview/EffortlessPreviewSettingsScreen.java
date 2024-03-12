package dev.huskuraft.effortless.screen.preview;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.settings.SettingButtonsList;

public class EffortlessPreviewSettingsScreen extends AbstractScreen {

    public EffortlessPreviewSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.settings.title"));
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, 35 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingButtonsList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 36));
        entries.addTab(Text.translate("effortless.preview_settings.title"), (button) -> {
            new EffortlessGeneralSettingsScreen(getEntrance()).attach();
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.15f, 0.7f).build());

    }

}
