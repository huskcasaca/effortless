package dev.huskuraft.effortless.screen.test;

import java.util.logging.Logger;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;

public class EffortlessTestScreen extends AbstractContainerScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"), CONTAINER_WIDTH_EXPANDED, CONTAINER_HEIGHT_270);
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + TITLE_CONTAINER - 10, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), getLeft() + PADDINGS, getTop() + TITLE_CONTAINER, getWidth() - PADDINGS * 2, getHeight() - TITLE_CONTAINER - BUTTON_CONTAINER_ROW_1));

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
                                new PatternConfig(),
                                new TransformerPresets(),
                                false
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
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth(), 20, Text.text("Open EffortlessPatternSimpleSettingsScreen"), button -> {
                new EffortlessPatternSettingsScreen(getEntrance()).attach();
            }));
        });
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
