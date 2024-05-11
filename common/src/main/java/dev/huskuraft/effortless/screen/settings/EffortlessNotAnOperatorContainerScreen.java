package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class EffortlessNotAnOperatorContainerScreen extends AbstractContainerScreen {

    public EffortlessNotAnOperatorContainerScreen(Entrance entrance) {
        super(entrance, Text.empty(), AbstractContainerScreen.CONTAINER_WIDTH_THIN, AbstractContainerScreen.CONTAINER_HEIGHT_THIN);
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 32, Text.translate("effortless.not_an_operator.title"), TextWidget.Gravity.CENTER));
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 16, Text.translate("effortless.not_an_operator.message"), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBounds(getWidth() / 2 - Button.TAB_WIDTH / 2, getHeight() / 2 + 32, Button.TAB_WIDTH, Button.DEFAULT_HEIGHT).build());

    }

}
