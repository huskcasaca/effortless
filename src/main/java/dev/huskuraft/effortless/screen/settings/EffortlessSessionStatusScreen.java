package dev.huskuraft.effortless.screen.settings;

import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.Dimens;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;

public class EffortlessSessionStatusScreen extends AbstractPanelScreen {

    public EffortlessSessionStatusScreen(Entrance entrance) {
        super(entrance, Text.empty(), PANEL_WIDTH_60, 0);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, Text.translate("effortless.session_status.title").withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        var entries = addWidget(new TextList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1));
        entries.reset(Stream.of(List.of(Text.empty()), getMessages(), List.of(Text.empty())).flatMap(List::stream).toList());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    private List<Text> getMessages() {
        return TooltipHelper.wrapLines(getTypeface(),
                switch (getEntrance().getSessionManager().getSessionStatus()) {
                    case MOD_MISSING -> Text.translate("effortless.session_status.message.mod_missing");
                    case SERVER_MOD_MISSING -> Text.translate("effortless.session_status.message.server_mod_missing");
                    case CLIENT_MOD_MISSING -> Text.translate("effortless.session_status.message.client_mod_missing");
                    case PROTOCOL_NOT_MATCH -> Text.translate("effortless.session_status.message.protocol_not_match", getEntrance().getSessionManager().getServerSession().protocolVersion(), getEntrance().getSessionManager().getLastSession().protocolVersion());
                    case SUCCESS -> Text.translate("effortless.session_status.message.success", getEntrance().getSessionManager().getServerSession().loaderType().name());
                }, getWidth() - PADDINGS_H * 2);
    }

    @Override
    public int getHeight() {
        return PANEL_TITLE_HEIGHT_1 + (getMessages().size() + 2) * 10 + 4 + Dimens.Screen.BUTTON_ROW_1;
    }
}
