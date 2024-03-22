package dev.huskuraft.effortless.screen.test;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.general.EffortlessGlobalGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.general.EffortlessPerPlayerGeneralSettingsListScreen;
import dev.huskuraft.effortless.screen.general.EffortlessPerPlayerGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;

public class EffortlessTestScreen extends AbstractScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"));
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 36));

        entries.addSimpleEntry(entry -> {
            var editBox = entry.addWidget(new EditBox(getEntrance(), entry.getX() + entry.getWidth() / 2 - 100 - 38, entry.getY(), 210, 20, Text.empty()));
            entry.addWidget(new Button(getEntrance(), entry.getCenterX() + 100 - 26, entry.getY(), 64, 20, Text.text("Execute"), (button) -> {
                getEntrance().getClient().sendCommand(editBox.getValue());
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth() / 2, 20, Text.text("Load Toml Config"), (button) -> {

            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + entry.getWidth() / 2 , entry.getTop(), entry.getWidth() / 2, 20, Text.text("Save Toml Config"), (button) -> {
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW, 20, Text.text("Open EffortlessSettingsScreen"), (button) -> {
                new EffortlessSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 20, Dimens.Buttons.ROW, 20, Text.text("Open EffortlessGeneralSettingsScreen"), (button) -> {
                new EffortlessGeneralSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 40, Dimens.Buttons.ROW, 20, Text.text("Open EffortlessGlobalGeneralSettingsScreen"), (button) -> {
                new EffortlessGlobalGeneralSettingsScreen(getEntrance(), getEntrance().getSessionManager().getServerSessionConfig().getGlobalConfig(), config -> {
                    getEntrance().getSessionManager().updateGlobalConfig(config);
                }).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 60, Dimens.Buttons.ROW, 20, Text.text("Open EffortlessPerPlayerGeneralSettingsScreen"), (button) -> {
                new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {
                    new EffortlessPerPlayerGeneralSettingsScreen(getEntrance(), playerInfo, getEntrance().getSessionManager().getServerSessionConfig().getGlobalConfig(), getEntrance().getSessionManager().getServerSessionConfig().getPlayerConfig(playerInfo.getId()), (playerInfo1, config) -> {
                        getEntrance().getSessionManager().updatePlayerConfig(playerInfo1.getId(), config);
                    }).attach();
                }).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 80, Dimens.Buttons.ROW, 20, Text.text("Open EffortlessPlayerSearchScreen"), (button) -> {
                new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {

                }).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 100, Dimens.Buttons.ROW, 20, Text.text("Open EffortlessPlayerSearchScreen"), (button) -> {
                new EffortlessPerPlayerGeneralSettingsListScreen(getEntrance(), getEntrance().getSessionManager().getConfigurablePlayers(), playerInfo -> {
                    new EffortlessPerPlayerGeneralSettingsScreen(getEntrance(), playerInfo, getEntrance().getSessionManager().getServerSessionConfig().getGlobalConfig(), getEntrance().getSessionManager().getServerSessionConfig().getPlayerConfig(playerInfo.getId()), (playerInfo1, config) -> {
                        getEntrance().getSessionManager().updatePlayerConfig(playerInfo1.getId(), config);
                    }).attach();
                }).attach();
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW, 20, Text.text("Open EffortlessPatternSettingsScreen"), (button) -> {
                new EffortlessPatternSettingsScreen(getEntrance()).attach();
            }));
        });
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
