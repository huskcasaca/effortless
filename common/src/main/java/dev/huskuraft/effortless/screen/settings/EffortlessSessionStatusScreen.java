package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public class EffortlessSessionStatusScreen extends AbstractScreen {

    public EffortlessSessionStatusScreen(Entrance entrance) {
        super(entrance);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 32, Text.translate("effortless.session_status.title"), TextWidget.Gravity.CENTER));
        var message = switch (getEntrance().getSessionManager().getSessionStatus()) {
            case MOD_MISSING -> Text.translate("effortless.session_status.message.mod_missing");
            case SERVER_MOD_MISSING -> Text.translate("effortless.session_status.message.server_mod_missing");
            case CLIENT_MOD_MISSING -> Text.translate("effortless.session_status.message.client_mod_missing");
            case PROTOCOL_NOT_MATCH -> Text.translate("effortless.session_status.message.protocol_not_match", getEntrance().getSessionManager().getServerSession().protocolVersion(), getEntrance().getSessionManager().getLastSession().protocolVersion());
            case SUCCESS -> Text.translate("effortless.session_status.message.success", getEntrance().getSessionManager().getServerSession().loaderType().name());
        };
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, getHeight() / 2 - 16, message, TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBounds(getWidth() / 2 - Button.TAB_WIDTH / 2, getHeight() / 2 + 32, Button.TAB_WIDTH, Button.DEFAULT_HEIGHT).build());

    }

}
