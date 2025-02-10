package dev.huskuraft.effortless.screen.settings;

import java.util.ArrayList;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.EffortlessClientConfigStorage;
import dev.huskuraft.effortless.EffortlessConfigStorage;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.networking.packets.player.PlayerPermissionCheckPacket;
import dev.huskuraft.effortless.screen.builer.EffortlessBuilderSettingsScreen;
import dev.huskuraft.effortless.screen.general.EffortlessGlobalGeneralSettingsScreen;

public class EffortlessSettingsScreen extends AbstractPanelScreen {

    public EffortlessSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.settings.title"), PANEL_WIDTH_42, PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3);
    }

    private Button constraintButton;
    private Button buildertButton;
//    private Button patternButton;
//    private Button clipboardButton;
//    private Button renderButton;

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.constraintButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.general_settings.title"), button -> {
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
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 2f   , 0f, 1f).build());
        this.buildertButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.builder_settings.title"), button -> {
            new EffortlessBuilderSettingsScreen(getEntrance()).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());
//        this.renderButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.render_settings.title"), button -> {
//            new EffortlessRenderSettingsScreen(getEntrance()).attach();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());
//        this.patterntButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern_settings.title"), button -> {
//            new EffortlessPatternSettingsScreen(getEntrance()).attach();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 2f, 0f, 1f).build());
//        this.clipboardButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.clipboard_settings.title"), button -> {
//            new EffortlessClipboardSettingsScreen(getEntrance()).attach();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    @Override
    public void onReload() {

        var constraintTooltip = new ArrayList<Text>();
        constraintTooltip.add(Text.translate("effortless.general_settings.title").withStyle(ChatFormatting.WHITE));
        constraintTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            constraintTooltip.add(Text.empty());
            constraintTooltip.addAll(
                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.general_settings.tooltip", Text.text("[%s]".formatted(EffortlessConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
            );
        }
        this.constraintButton.setTooltip(constraintTooltip);

        var builderTooltip = new ArrayList<Text>();
        builderTooltip.add(Text.translate("effortless.builder_settings.title").withStyle(ChatFormatting.WHITE));
        builderTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            builderTooltip.add(Text.empty());
            builderTooltip.addAll(
                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.builder_settings.tooltip", Text.text("[%s]".formatted(EffortlessClientConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
            );
        }
        this.buildertButton.setTooltip(builderTooltip);

//        var patternTooltip = new ArrayList<Text>();
//        patternTooltip.add(Text.translate("effortless.pattern_settings.title").withStyle(ChatFormatting.WHITE));
//        patternTooltip.add(TooltipHelper.holdShiftForSummary());
//        if (TooltipHelper.isSummaryButtonDown()) {
//            patternTooltip.add(Text.empty());
//            patternTooltip.addAll(
//                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.pattern_settings.tooltip", Text.text("[%s]".formatted(EffortlessClientConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
//            );
//        }
//        this.patterntButton.setTooltip(patternTooltip);
//
//        var renderTooltip = new ArrayList<Text>();
//        renderTooltip.add(Text.translate("effortless.render_settings.title").withStyle(ChatFormatting.WHITE));
//        renderTooltip.add(TooltipHelper.holdShiftForSummary());
//        if (TooltipHelper.isSummaryButtonDown()) {
//            renderTooltip.add(Text.empty());
//            renderTooltip.addAll(
//                    TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.render_settings.tooltip", Text.text("[%s]".formatted(EffortlessClientConfigStorage.CONFIG_NAME)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY))
//            );
//        }
//        this.renderButton.setTooltip(renderTooltip);
    }
}
