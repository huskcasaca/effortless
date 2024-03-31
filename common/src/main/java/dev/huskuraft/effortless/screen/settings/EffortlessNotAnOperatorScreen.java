package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class EffortlessNotAnOperatorScreen extends AbstractScreen {

    public EffortlessNotAnOperatorScreen(Entrance entrance) {
        super(entrance);
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 32, Text.translate("effortless.not_an_operator.title"), TextWidget.Gravity.CENTER));
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 16, Text.translate("effortless.not_an_operator.message"), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBounds(getWidth() / 2 - Dimens.Buttons.TAB / 2, getHeight() / 2 + 32, Dimens.Buttons.TAB, Button.DEFAULT_HEIGHT).build());

    }

}
