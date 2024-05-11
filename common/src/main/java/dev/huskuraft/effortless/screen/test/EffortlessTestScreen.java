package dev.huskuraft.effortless.screen.test;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.container.SimpleEntryList;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.screen.general.EffortlessGeneralSettingsScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsContainerScreen;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsContainerScreen;

public class EffortlessTestScreen extends AbstractScreen {

    public EffortlessTestScreen(Entrance entrance) {
        super(entrance, Text.text("Test"));
    }

    @Override
    public void onCreate() {

        addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.test.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 1f).build());

        var entries = addWidget(new SimpleEntryList(getEntrance(), 0, Dimens.Screen.TITLE_36, getWidth(), getHeight() - Dimens.Screen.TITLE_36 - Dimens.Screen.BUTTON_ROW_1));

        entries.addSimpleEntry(entry -> {
            var editBox = entry.addWidget(new EditBox(getEntrance(), entry.getX() + entry.getWidth() / 2 - 100 - 38, entry.getY(), 210, 20, Text.empty()));
            entry.addWidget(new Button(getEntrance(), entry.getCenterX() + 100 - 26, entry.getY(), 64, 20, Text.text("Execute"), button -> {
                getEntrance().getClient().sendCommand(editBox.getValue());
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getTop(), entry.getWidth() / 2, 20, Text.text("Load Toml Config"), button -> {
                Logger.getAnonymousLogger().info("" + getEntrance().getConfigStorage().get());
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft() + entry.getWidth() / 2, entry.getTop(), entry.getWidth() / 2, 20, Text.text("Save Toml Config"), button -> {
                getEntrance().getConfigStorage().set(
                        new ClientConfig(
                                new RenderConfig(),
                                new PatternConfig(
                                        List.of(
                                                new Pattern(
                                                        true,
                                                        List.of(
                                                                new ArrayTransformer(new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), new Random().nextInt()),
                                                                new ArrayTransformer(new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), new Random().nextInt()),
                                                                new MirrorTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], Axis.values()[new Random().nextInt(Axis.values().length)]),
                                                                new MirrorTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], Axis.values()[new Random().nextInt(Axis.values().length)]),
                                                                new MirrorTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], Axis.values()[new Random().nextInt(Axis.values().length)]),
                                                                new RadialTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], new Random().nextInt(100)),
                                                                new RadialTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], new Random().nextInt(100)),
                                                                new RadialTransformer(UUID.randomUUID(), Text.text("" + new Random().nextDouble()), new Vector3d(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble()), PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], PositionType.values()[new Random().nextInt(PositionType.values().length)], new Random().nextInt(100))
                                                        )
                                                )
                                        )
                                ),
                                new TransformerPresets(
                                        List.of(),
                                        List.of(),
                                        List.of(),
                                        ItemRandomizer.getDefaultItemRandomizers()),
                                false
                        )
                );
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessSettingsScreen"), button -> {
                new EffortlessSettingsContainerScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 20, Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessGeneralSettingsScreen"), button -> {
                new EffortlessGeneralSettingsScreen(getEntrance()).attach();
            }));
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY() + 80, Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessOnlinePlayersScreen"), button -> {
                new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {

                }).attach();
            }));
        });
        entries.addSimpleEntry(entry -> {
            entry.addWidget(new Button(getEntrance(), entry.getLeft(), entry.getY(), Dimens.Buttons.ROW_WIDTH, 20, Text.text("Open EffortlessPatternSimpleSettingsScreen"), button -> {
                new EffortlessPatternSettingsContainerScreen(getEntrance()).attach();
            }));
        });
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
