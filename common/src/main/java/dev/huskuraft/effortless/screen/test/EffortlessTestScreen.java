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

public class EffortlessTestScreen extends AbstractScreen {

    private TextWidget titleTextWidget;
    private SimpleEntryList entries;
    private Button cancelButton;

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"));
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 1f).build());

        this.entries = addWidget(new SimpleEntryList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));

        entries.addEntry(new CommandExecutionEntry(getEntrance()));
        entries.addEntry(new TomlConfigEntry(getEntrance()));
    }

    private static class CommandExecutionEntry extends SimpleEntryList.Entry {

        public CommandExecutionEntry(Entrance entrance) {
            super(entrance);
        }

        @Override
        public void onCreate() {

            var editBox = addWidget(new EditBox(getEntrance(), getX() + getWidth() / 2 - 100 - 38, getY(), 210, 20, Text.empty()));

            addWidget(new Button(getEntrance(), getX() + getWidth() / 2 + 100 - 26, getY(), 64, 20, Text.text("Execute"), (button) -> {
                getEntrance().getClient().sendCommand(editBox.getValue());

            }));
        }
    }

    private static class TomlConfigEntry extends SimpleEntryList.Entry {

        public TomlConfigEntry(Entrance entrance) {
            super(entrance);
        }

        @Override
        public void onCreate() {

            addWidget(new Button(getEntrance(), getX() + getWidth() / 2 - 118, getY(), 128, 20, Text.text("Load Toml Config"), (button) -> {
                Logger.getAnonymousLogger().info("" + Effortless.getInstance().getSessionConfigStorage().get());

            }));

            addWidget(new Button(getEntrance(), getX() + getWidth() / 2 - 130 + 68, getY(), 128, 20, Text.text("Save Toml Config"), (button) -> {
                Effortless.getInstance().getSessionConfigStorage().set(Effortless.getInstance().getSessionConfigStorage().get());
            }));
        }
    }

}
