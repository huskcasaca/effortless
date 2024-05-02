package dev.huskuraft.effortless.screen.settings;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.networking.packets.player.PlayerOperatorCheckPacket;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsScreen;
import dev.huskuraft.effortless.screen.preview.EffortlessRenderSettingsScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerTemplateSelectScreen;

public class EffortlessSettingsScreen extends AbstractScreen {

    public EffortlessSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.settings.title"));
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        var entries = addWidget(new SettingButtonsList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_1));
        entries.addTab(Text.translate("effortless.general_settings.title"), (button) -> {
            if (!getEntrance().getSessionManager().isSessionValid()) {
                getEntrance().getClient().execute(() -> {
                    new EffortlessSessionStatusScreen(getEntrance()).attach();
                });
            } else {
                getEntrance().getChannel().sendPacket(new PlayerOperatorCheckPacket(getEntrance().getClient().getPlayer().getId()), (packet) -> {
                    if (packet.isOperator()) {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessGeneralSettingsScreen(getEntrance()).attach();
                        });
                    } else {
                        getEntrance().getClient().execute(() -> {
                            new EffortlessNotAnOperatorScreen(getEntrance()).attach();
                        });
                    }
                });
            }
        });
        entries.addTab(Text.translate("effortless.render_settings.title"), (button) -> {
            new EffortlessRenderSettingsScreen(getEntrance()).attach();
        });
        entries.addTab(Text.translate("effortless.pattern_settings.title"), (button) -> {
            new EffortlessPatternSettingsScreen(getEntrance()).attach();
        });
        entries.addTab(Text.translate("effortless.transformer_presets.title"), (button) -> {
            new EffortlessTransformerTemplateSelectScreen(
                    getEntrance(),
                    transformer -> {
                    },
                    Transformer.getDefaultTransformers()
            ).attach();
        });
        entries.addTab(Text.translate("effortless.settings.passive_mode").getString() + getEntrance().getConfigStorage().get().passiveMode(), (button) -> {
            getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.patternConfig(), config.transformerPresets(), !config.passiveMode()));
            button.setMessage(Text.translate("effortless.settings.passive_mode").getString() + getEntrance().getConfigStorage().get().passiveMode());
        });

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBounds(getWidth() / 2 - Button.TAB_WIDTH / 2, getHeight() - Button.DEFAULT_HEIGHT - Button.MARGIN, Button.TAB_WIDTH, Button.DEFAULT_HEIGHT).build());

    }

}
