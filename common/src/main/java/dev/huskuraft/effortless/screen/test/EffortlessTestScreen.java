package dev.huskuraft.effortless.screen.test;

import java.util.logging.Logger;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsContainerScreen;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;

public class EffortlessTestScreen extends AbstractScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"));
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_1));

        entries.addSimpleEntry(entry -> {
            var editBox = entry.addWidget(new EditBox(getEntrance(), entry.getX() + entry.getWidth() / 2 - 100 - 38, entry.getY(), 210, 20, Text.empty()));
            entry.addWidget(new Button(getEntrance(), entry.getCenterX() + 100 - 26, entry.getY(), 64, 20, Text.text("Execute"), button -> {
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
                                new PatternConfig(),
                                new TransformerPresets(),
                                false
                        )
                );
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessSettingsScreen"), button -> {
                new EffortlessSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 20, Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessGeneralSettingsScreen"), button -> {
                new EffortlessGeneralSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 80, Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessOnlinePlayersScreen"), button -> {
                new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {

                }).attach();
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessPatternSimpleSettingsScreen"), button -> {
                new EffortlessPatternSettingsContainerScreen(getEntrance()).attach();
            }));
        });
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
