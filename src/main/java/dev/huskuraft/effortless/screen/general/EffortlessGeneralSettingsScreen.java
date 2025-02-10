package dev.huskuraft.effortless.screen.general;

import java.util.ArrayList;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.EffortlessConfigStorage;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.networking.packets.player.PlayerPermissionCheckPacket;
import dev.huskuraft.effortless.screen.settings.EffortlessNotAnOperatorScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSessionStatusScreen;

public class EffortlessGeneralSettingsScreen extends AbstractPanelScreen {

    public EffortlessGeneralSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.general_settings.title"), PANEL_WIDTH_42, PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3);
    }

    private Button globalButton;
    private Button playerButton;

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.globalButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.global_general_settings.title"), button -> {
            if (!getEntrance().getSessionManager().isSessionValid()) {
                getEntrance().getClient().execute(() -> {
                    new EffortlessSessionStatusScreen(getEntrance()).attach();
                });
            } else {
                getEntrance().getChannel().sendPacket(new PlayerPermissionCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {
                    if (packet.granted()) {
                        getEntrance().getClient().execute(() -> {
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
        this.playerButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.player_general_settings.title"), button -> {
            getEntrance().getChannel().sendPacket(new PlayerPermissionCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {

                if (!getEntrance().getSessionManager().isSessionValid()) {
                    getEntrance().getClient().execute(() -> {
                        new EffortlessSessionStatusScreen(getEntrance()).attach();
                    });
                } else {
                    if (packet.granted()) {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessPlayerGeneralSettingsListScreen(getEntrance(), getEntrance().getSessionManager().getServerSessionConfigOrEmpty().playerConfigs(), playerConfigs -> {
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

    @Override
    public void onReload() {
        var globalTooltip = new ArrayList<Text>();
        globalTooltip.add(Text.translate("effortless.global_general_settings.title").withStyle(ChatFormatting.WHITE));
        globalTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            globalTooltip.add(Text.empty());
            globalTooltip.addAll(
                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.global_general_settings.tooltip", Text.text("[%s]".formatted(EffortlessConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
            );
        }
        this.globalButton.setTooltip(globalTooltip);


        var playerTooltip = new ArrayList<Text>();
        playerTooltip.add(Text.translate("effortless.player_general_settings.title").withStyle(ChatFormatting.WHITE));
        playerTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            playerTooltip.add(Text.empty());
            playerTooltip.addAll(
                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.player_general_settings.tooltip", Text.text("[%s]".formatted(EffortlessConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
            );
        }
        this.playerButton.setTooltip(playerTooltip);
    }
}
