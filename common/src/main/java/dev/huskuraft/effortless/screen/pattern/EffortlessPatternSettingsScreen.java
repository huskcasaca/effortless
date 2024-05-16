package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerPresetsScreen;

public class EffortlessPatternSettingsScreen extends AbstractPanelScreen {

    public EffortlessPatternSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.pattern_settings.title"), PANEL_WIDTH, PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_2);
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer_presets.title"), button -> {
            new EffortlessTransformerPresetsScreen(getEntrance()).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
