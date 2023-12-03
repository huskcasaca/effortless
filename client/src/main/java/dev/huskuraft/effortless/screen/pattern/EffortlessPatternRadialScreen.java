package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.history.UndoRedo;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.settings.Settings;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.input.Key;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;
import dev.huskuraft.effortless.text.Text;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class EffortlessPatternRadialScreen extends AbstractRadialScreen<Pattern, Option> {

    private static final Button<Option> UNDO_OPTION = option(UndoRedo.UNDO);
    private static final Button<Option> REDO_OPTION = option(UndoRedo.REDO);
    private static final Button<Option> SETTING_OPTION = option(Settings.MODE_SETTINGS);
    private static final Button<Option> REPLACE_OPTION = option(ReplaceMode.DISABLED);

    private final Key assignedKey;

    public EffortlessPatternRadialScreen(Entrance entrance, Key assignedKey) {
        super(entrance, Text.translate(Text.asKey("screen", "pattern_radial")));
        this.assignedKey = assignedKey;
    }

    public static Slot<Pattern> slot(Pattern pattern) {
        return slot(
                pattern.name(),
                pattern == Pattern.DISABLED ? BuildMode.DISABLED.getIcon() : BuildMode.SPHERE.getIcon(),
                new Color(0.25f, 0.25f, 0.25f, 0.5f),
                pattern
        );
    }

    public Key getAssignedKey() {
        return assignedKey;
    }

    private void selectPattern(Pattern pattern) {
        getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), pattern);
    }

    private void selectBuildFeature(SingleSelectFeature feature) {
        getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), feature);
    }

    private void selectBuildFeature(MultiSelectFeature feature) {
        getEntrance().getStructureBuilder().setBuildFeature(getEntrance().getClient().getPlayer(), feature);
    }

    private List<Slot<Pattern>> getSlots() {
        var settingPatterns = getEntrance().getConfigManager().getConfig().getPatternConfig().getPatterns();
        return Stream.concat(
                Stream.concat(Stream.of(Pattern.DISABLED), settingPatterns.stream()),
                Stream.generate(() -> Pattern.RESERVED).limit(Math.max(12 - settingPatterns.size() - 1, 0))
        ).map(EffortlessPatternRadialScreen::slot).toList();
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
                getSlots()
        );
        setRadialSelectResponder(slot -> {
            if (slot.getContent() == Pattern.RESERVED) {

            } else {
                selectPattern(slot.getContent());
            }
        });
//        setRadialOptionSelectResponder(entry -> {
//            if (entry.getContent() instanceof SingleSelectFeature) {
//                selectBuildFeature((SingleSelectFeature) entry.getContent());
//            }
//            if (entry.getContent() instanceof MultiSelectFeature) {
//                selectBuildFeature((MultiSelectFeature) entry.getContent());
//            }
//
//        });
    }

    @Override
    public void onReload() {
        var context = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer());
        setSelectedSlots(slot(context.pattern()));
    }

}

