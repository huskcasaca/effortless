package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.networking.packets.player.PlayerOperatorCheckPacket;
import dev.huskuraft.effortless.screen.general.EffortlessSimpleGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessSimplePatternSettingsScreen;
import dev.huskuraft.effortless.screen.preview.EffortlessRenderSettingsScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerTemplateSelectScreen;

public class EffortlessSimpleSettingsScreen extends AbstractPanelScreen {

    public EffortlessSimpleSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.settings.title"), Dimens.Screen.CONTAINER_WIDTH_THIN, Dimens.Screen.CONTAINER_HEIGHT_THIN);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        setContainerHeight(Dimens.Screen.TITLE_CONTAINER + Dimens.Screen.BUTTON_CONTAINER_ROW_6);
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + Dimens.Screen.TITLE_CONTAINER - 12, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.general_settings.title"), button -> {
            if (!getEntrance().getSessionManager().isSessionValid()) {
                getEntrance().getClient().execute(() -> {
                    new EffortlessSessionStatusScreen(getEntrance()).attach();
                });
            } else {
                getEntrance().getChannel().sendPacket(new PlayerOperatorCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {
                    if (packet.isOperator()) {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessSimpleGeneralSettingsScreen(getEntrance()).attach();
                        });
                    } else {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessNotAnOperatorScreen(getEntrance()).attach();
                        });
                    }
                });
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 5f, 0f, 1f).build());
        addWidget(Button.builder(getEntrance(), Text.translate("effortless.render_settings.title"), button -> {
            new EffortlessRenderSettingsScreen(getEntrance()).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 4f, 0f, 1f).build());
        addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern_settings.title"), button -> {
            new EffortlessSimplePatternSettingsScreen(getEntrance()).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 3f, 0f, 1f).build());
        addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer_presets.title"), button -> {
            new EffortlessTransformerTemplateSelectScreen(
                    getEntrance(),
                    transformer -> {
                    },
                    Transformer.getDefaultTransformers()
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 2f, 0f, 1f).build());
        addWidget(Button.builder(getEntrance(), Text.text(Text.translate("effortless.settings.passive_mode").getString() + getEntrance().getConfigStorage().get().passiveMode()), button -> {
            getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.patternConfig(), config.transformerPresets(), !config.passiveMode()));
            button.setMessage(Text.translate("effortless.settings.passive_mode").getString() + getEntrance().getConfigStorage().get().passiveMode());
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

}
