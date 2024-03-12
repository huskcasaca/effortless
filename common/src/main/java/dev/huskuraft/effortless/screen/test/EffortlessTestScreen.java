package dev.huskuraft.effortless.screen.test;

import java.util.logging.Logger;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.general.EffortlessGlobalGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.general.EffortlessPerPlayerGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;
import dev.huskuraft.effortless.session.config.BuildingConfig;

public class EffortlessTestScreen extends AbstractScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"));
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));

        entries.addSimpleEntry(entry -> {
            var editBox = entry.addWidget(new EditBox(getEntrance(), entry.getX() + entry.getWidth() / 2 - 100 - 38, entry.getY(), 210, 20, Text.empty()));
            entry.addWidget(new Button(getEntrance(), entry.getCenterX() + 100 - 26, entry.getY(), 64, 20, Text.text("Execute"), (button) -> {
                getEntrance().getClient().sendCommand(editBox.getValue());
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth() / 2, 20, Text.text("Load Toml Config"), (button) -> {
                Logger.getAnonymousLogger().info("" + Effortless.getInstance().getSessionConfigStorage().get());

            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + entry.getWidth() / 2 , entry.getTop(), entry.getWidth() / 2, 20, Text.text("Save Toml Config"), (button) -> {
                Effortless.getInstance().getSessionConfigStorage().set(Effortless.getInstance().getSessionConfigStorage().get());
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), 256, 20, Text.text("Open EffortlessSettingsScreen"), (button) -> {
                new EffortlessSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + 8, entry.getY() + 20, 256, 20, Text.text("Open EffortlessGeneralSettingsScreen"), (button) -> {
                new EffortlessGeneralSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + 16, entry.getY() + 40, 256, 20, Text.text("Open EffortlessGlobalGeneralSettingsScreen"), (button) -> {
                new EffortlessGlobalGeneralSettingsScreen(getEntrance(), BuildingConfig.DEFAULT, config -> {

                }).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + 16, entry.getY() + 60, 256, 20, Text.text("Open EffortlessGlobalGeneralSettingsScreen"), (button) -> {
                new EffortlessPerPlayerGeneralSettingsScreen(getEntrance(), BuildingConfig.DEFAULT, BuildingConfig.DEFAULT, config -> {

                }).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + 8, entry.getY() + 80, 256, 20, Text.text("Open EffortlessPatternSettingsScreen"), (button) -> {
                new EffortlessPatternSettingsScreen(getEntrance()).attach();
            }));
        });
    }

}
