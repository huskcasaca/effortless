package dev.huskuraft.effortless.screen.test;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.AbstractEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class EffortlessTestScreen extends AbstractScreen {

    private TextWidget titleTextWidget;
    private AbstractEntryList entries;
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

        var editBox = addWidget(new EditBox(getEntrance(), getX() + getWidth() / 2 - 150, getY() + 32, 260, 20, Text.empty()));

        addWidget(new Button(getEntrance(), getX() + getWidth() / 2 + 118, getY() + 32, 64, 20, Text.text("Execute"), (button) -> {
            getEntrance().getClient().sendCommand(editBox.getValue());

        }));

    }


    static class EditField extends AbstractEntryList.Entry {
        protected EditField(Entrance entrance) {
            super(entrance);
        }

        @Override
        public void onCreate() {
        }
    }

}
