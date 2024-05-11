package dev.huskuraft.effortless.screen.general;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.networking.packets.player.PlayerOperatorCheckPacket;
import dev.huskuraft.effortless.screen.settings.EffortlessNotAnOperatorScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSessionStatusScreen;

public class EffortlessGeneralSettingsContainerScreen extends AbstractContainerScreen {

    public EffortlessGeneralSettingsContainerScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.general_settings.title"), AbstractContainerScreen.CONTAINER_WIDTH_THIN, Dimens.Screen.TITLE_CONTAINER + AbstractContainerScreen.BUTTON_CONTAINER_ROW_3);
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + Dimens.Screen.TITLE_CONTAINER - 10, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.global_general_settings.title"), button -> {
            if (!getEntrance().getSessionManager().isSessionValid()) {
                getEntrance().getClient().execute(() -> {
                    new EffortlessSessionStatusScreen(getEntrance()).attach();
                });
            } else {
                getEntrance().getChannel().sendPacket(new PlayerOperatorCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {
                    if (packet.isOperator()) {
                        getEntrance().getClient().execute(() -> {
                            detach();
                            new EffortlessGlobalGeneralSettingsScreen(getEntrance(), getEntrance().getSessionManager().getServerSessionConfigOrEmpty().getGlobalConfig(), config -> {
                                getEntrance().getSessionManager().updateGlobalConfig(config);
                            }).attach();
                        });
                    } else {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessNotAnOperatorScreen(getEntrance()).attach();
                        });
                    }
                });
            }

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 2f, 0f, 1f).build());
        addWidget(Button.builder(getEntrance(), Text.translate("effortless.per_player_general_settings.title"), button -> {
            getEntrance().getChannel().sendPacket(new PlayerOperatorCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {

                if (!getEntrance().getSessionManager().isSessionValid()) {
                    getEntrance().getClient().execute(() -> {
                        new EffortlessSessionStatusScreen(getEntrance()).attach();
                    });
                } else {
                    if (packet.isOperator()) {
                        detach();
                        getEntrance().getClient().execute(() -> {
                            new EffortlessPerPlayerGeneralSettingsListScreen(getEntrance(), getEntrance().getSessionManager().getServerSessionConfigOrEmpty().playerConfigs(), playerConfigs -> {
                                getEntrance().getSessionManager().updatePlayerConfig(playerConfigs);
                            }).attach();
                        });
                    } else {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessNotAnOperatorScreen(getEntrance()).attach();
                        });
                    }
                }
            });
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
