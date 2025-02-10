package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.config.ClipboardConfig;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

import java.util.function.Consumer;

public class EffortlessClipboardSettingsScreen extends AbstractPanelScreen {

    private final Consumer<ClipboardConfig> consumer;
    private ClipboardConfig originalConfig;
    private ClipboardConfig config;

    public EffortlessClipboardSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.clipboard_settings.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.consumer = newConfig -> {
            getEntrance().getConfigStorage().update(config -> config.withClipboardConfig(newConfig));
        };
        this.config = getEntrance().getConfigStorage().get().clipboardConfig();
        this.originalConfig = config;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1, false, false));


        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
