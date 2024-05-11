package dev.huskuraft.effortless.screen.settings;

import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;

public class EffortlessSessionStatusScreen extends AbstractContainerScreen {

    public EffortlessSessionStatusScreen(Entrance entrance) {
        super(entrance, Text.empty(), AbstractContainerScreen.CONTAINER_WIDTH, 0);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + AbstractContainerScreen.TITLE_CONTAINER - 10, Text.translate("effortless.session_status.title").withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        var entries = addWidget(new TextList(getEntrance(), getLeft() + PADDINGS, getTop() + AbstractContainerScreen.TITLE_CONTAINER, getWidth() - PADDINGS * 2, getHeight() - AbstractContainerScreen.TITLE_CONTAINER - AbstractContainerScreen.BUTTON_CONTAINER_ROW_1));
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
                }, getWidth() - PADDINGS * 2);
    }

    @Override
    public int getHeight() {
        return AbstractContainerScreen.TITLE_CONTAINER + (getMessages().size() + 2) * 10 + 4 + Dimens.Screen.BUTTON_ROW_1;
    }
}
