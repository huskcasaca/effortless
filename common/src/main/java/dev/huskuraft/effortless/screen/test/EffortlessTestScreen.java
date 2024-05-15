package dev.huskuraft.effortless.screen.test;

import java.util.logging.Logger;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternScreen;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;

public class EffortlessTestScreen extends AbstractPanelScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), getLeft() + PADDINGS, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1));

        entries.addSimpleEntry(entry -> {
            var editBox = entry.addWidget(new EditBox(getEntrance(), entry.getLeft(), entry.getY(), entry.getWidth() - 64, 20, Text.empty()));
            entry.addWidget(new Button(getEntrance(), entry.getRight() - 64, entry.getTop(), 64, 20, Text.text("Execute"), button -> {
                getEntrance().getClient().sendCommand(editBox.getValue());
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth() / 2, 20, Text.text("Load Toml Config"), button -> {
                Logger.getAnonymousLogger().info("" + getEntrance().getConfigStorage().get());
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + entry.getWidth() / 2, entry.getTop(), entry.getWidth() / 2, 20, Text.text("Save Toml Config"), button -> {
                getEntrance().getConfigStorage().set(
                        new ClientConfig(
                                new RenderConfig(),
                                new PatternConfig()
                        )
                );
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth(), 20, Text.text("Open EffortlessSettingsScreen"), button -> {
                new EffortlessSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop() + 20, entry.getWidth(), 20, Text.text("Open EffortlessGeneralSettingsScreen"), button -> {
                new EffortlessGeneralSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop() + 80, entry.getWidth(), 20, Text.text("Open EffortlessOnlinePlayersScreen"), button -> {
                new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {

                }).attach();
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth(), 20, Text.text("Open EffortlessPatternScreen"), button -> {
                new EffortlessPatternScreen(getEntrance()).attach();
            }));
        });
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
