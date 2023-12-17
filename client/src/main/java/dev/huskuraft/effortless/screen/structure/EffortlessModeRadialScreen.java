package dev.huskuraft.effortless.screen.structure;

import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.history.UndoRedo;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.settings.Settings;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.input.Key;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;
import dev.huskuraft.effortless.text.Text;

import java.util.Arrays;

public class EffortlessModeRadialScreen extends AbstractRadialScreen<BuildMode, Option> {

    private static final Button<Option> UNDO_OPTION = button(UndoRedo.UNDO);
    private static final Button<Option> REDO_OPTION = button(UndoRedo.REDO);
    private static final Button<Option> SETTING_OPTION = button(Settings.MODE_SETTINGS);
    private static final Button<Option> REPLACE_OPTION = button(ReplaceMode.DISABLED);

    private final Key assignedKey;

    public EffortlessModeRadialScreen(Entrance entrance, Key assignedKey) {
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

    public Key getAssignedKey() {
        return assignedKey;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getAssignedKey().isKeyDown()) {
            detach();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setLeftButtons(
                buttonSet(REDO_OPTION, UNDO_OPTION),
                buttonSet(SETTING_OPTION, REPLACE_OPTION)
        );
        setRadialSlots(
                Arrays.stream(BuildMode.values()).map(mode -> slot(mode)).toList()
        );
        setRadialSelectResponder(slot -> {
            getEntrance().getStructureBuilder().setBuildMode(getEntrance().getClient().getPlayer(), slot.getContent());
        });
        setRadialOptionSelectResponder(entry -> {
            if (entry.getContent() instanceof SingleSelectFeature singleSelectFeature) {
                getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), singleSelectFeature);
            }
            if (entry.getContent() instanceof MultiSelectFeature multiSelectFeature) {
                getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), multiSelectFeature);
            }

        });
    }

    @Override
    public void onReload() {
        var context = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer());

        setSelectedSlots(slot(context.buildMode()));
        setRightButtons(
                Arrays.stream(context.buildMode().getSupportedFeatures()).map(feature -> buttonSet(Arrays.stream(feature.getEntries()).map(EffortlessModeRadialScreen::<Option>button).toList())).toList()
        );
        setSelectedButtons(
                context.buildFeatures().stream().map(EffortlessModeRadialScreen::<Option>button).toList()
        );
    }

}

