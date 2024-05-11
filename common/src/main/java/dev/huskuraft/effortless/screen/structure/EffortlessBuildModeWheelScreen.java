package dev.huskuraft.effortless.screen.structure;

import java.util.Arrays;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.input.Key;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.Feature;
import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PassiveMode;
import dev.huskuraft.effortless.building.history.UndoRedo;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.settings.Settings;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternSettingsContainerScreen;
import dev.huskuraft.effortless.screen.radial.AbstractWheelScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsContainerScreen;

public class EffortlessBuildModeWheelScreen extends AbstractWheelScreen<BuildMode, Option> {

    private static final Button<Option> UNDO_OPTION = button(UndoRedo.UNDO, false);
    private static final Button<Option> REDO_OPTION = button(UndoRedo.REDO);
    private static final Button<Option> SETTING_OPTION = button(Settings.SETTINGS);
    private static final Button<Option> PATTERN_SETTINGS_OPTION = button(Settings.PATTERN_SETTINGS);

    private static final Button<Option> REPLACE_DISABLED_OPTION = button(ReplaceMode.DISABLED, false);
    private static final Button<Option> REPLACE_NORMAL_OPTION = button(ReplaceMode.NORMAL, true);
    private static final Button<Option> REPLACE_QUICK_OPTION = button(ReplaceMode.QUICK, true);

    private static final Button<Option> REPLACE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return switch (context.replaceMode()) {
            case DISABLED -> REPLACE_DISABLED_OPTION;
            case NORMAL -> REPLACE_NORMAL_OPTION;
            case QUICK -> REPLACE_QUICK_OPTION;
        };
    });


    private static final Button<Option> PASSIVE_MODE_DISABLED_OPTION = button(PassiveMode.DISABLED, false);
    private static final Button<Option> PASSIVE_MODE_ENABLED_OPTION = button(PassiveMode.ENABLED, true);

    private static final Button<Option> PASSIVE_MODE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        if (entrance.getConfigStorage().get().passiveMode()) {
            return PASSIVE_MODE_ENABLED_OPTION;
        } else {
            return PASSIVE_MODE_DISABLED_OPTION;
        }
    });

    private final Key assignedKey;

    public EffortlessBuildModeWheelScreen(Entrance entrance, Key assignedKey) {
        super(entrance, Text.translate("effortless.building.radial.title"));
        this.assignedKey = assignedKey;
    }

    public static Slot<BuildMode> slot(BuildMode mode) {
        return slot(
                mode,
                mode.getDisplayName(),
                mode.getIcon(),
                mode.getTintColor(),
                mode);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    public Key getAssignedKey() {
        return assignedKey;
    }

    @Override
    public void tick() {
        super.tick();
        Key key = getAssignedKey();
        if (!key.getBinding().isKeyDown()) {
            detach();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setRadialSlots(
                Arrays.stream(BuildMode.values()).map(EffortlessBuildModeWheelScreen::slot).toList()
        );
        setRadialSelectResponder(slot -> {
            getEntrance().getStructureBuilder().setBuildMode(getEntrance().getClient().getPlayer(), slot.getContent());
        });
        setRadialOptionSelectResponder(entry -> {
            if (entry.getContent() instanceof Settings settings) {
                switch (settings) {
                    case SETTINGS -> {
                        detach();
                        new EffortlessSettingsContainerScreen(getEntrance()).attach();
                    }
                    case PATTERN_SETTINGS -> {
                        detach();
                        new EffortlessPatternSettingsContainerScreen(getEntrance()).attach();
                    }
                }
                return;
            }
            if (entry.getContent() instanceof UndoRedo undoRedo) {
                switch (undoRedo) {
                    case UNDO -> {
                        getEntrance().getStructureBuilder().undo(getEntrance().getClient().getPlayer());
                    }
                    case REDO -> {
                        getEntrance().getStructureBuilder().redo(getEntrance().getClient().getPlayer());
                    }
                }
                return;
            }
            if (entry.getContent() instanceof ReplaceMode replaceMode) {
                getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), replaceMode.next());
                return;
            }
            if (entry.getContent() instanceof PassiveMode passiveMode) {
                getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.patternConfig(), config.transformerPresets(), passiveMode != PassiveMode.ENABLED));
                return;
            }
            if (entry.getContent() instanceof SingleSelectFeature singleSelectFeature) {
                getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), singleSelectFeature);
                return;
            }
            if (entry.getContent() instanceof MultiSelectFeature multiSelectFeature) {
                getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), multiSelectFeature);
                return;
            }
        });
    }

    @Override
    public void onReload() {
        var context = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer());

        setSelectedSlots(slot(context.buildMode()));
        setLeftButtons(
                buttonSet(REPLACE_OPTION, REDO_OPTION, UNDO_OPTION),
                buttonSet(PASSIVE_MODE_OPTION, PATTERN_SETTINGS_OPTION, SETTING_OPTION)
        );
        setRightButtons(
                Arrays.stream(context.buildMode().getSupportedFeatures()).map(feature -> buttonSet(Arrays.stream(feature.getEntries()).map((Feature option) -> button((Option) option, context.buildFeatures().contains(option))).toList())).toList()
        );
    }

}

