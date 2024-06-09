package dev.huskuraft.effortless.screen.builer;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.BuilderConfig;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessBuilderSettingsScreen extends AbstractPanelScreen {

    private final Consumer<BuilderConfig> consumer;
    private BuilderConfig originalConfig;
    private BuilderConfig config;


    public EffortlessBuilderSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.builder_settings.title"), PANEL_WIDTH_50, PANEL_HEIGHT_FULL);
        this.consumer = pattern -> {
            getEntrance().getConfigStorage().update(config -> config.withBuilderConfig(this.config));
        };
        this.config = getEntrance().getConfigStorage().get().builderConfig();
        this.originalConfig = config;
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1, false, false));
        entries.addIntegerEntry(Text.translate("effortless.builder_settings.reserved_tool_durability"), null, config.reservedToolDurability(), BuilderConfig.RESERVED_TOOL_DURABILITY_RANGE.min(), BuilderConfig.RESERVED_TOOL_DURABILITY_RANGE.max(), (value) -> {
            this.config = new BuilderConfig(value, config.passiveMode());
        });
        entries.addSwitchEntry(Text.translate("effortless.builder_settings.passive_mode"), null, config.passiveMode(), (value) -> {
            this.config = new BuilderConfig(config.reservedToolDurability(), value);
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
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
