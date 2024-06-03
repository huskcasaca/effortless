package dev.huskuraft.effortless.screen.structure;

import java.util.ArrayList;
import java.util.Arrays;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.input.Key;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.Feature;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.config.PassiveMode;
import dev.huskuraft.effortless.building.history.UndoRedo;
import dev.huskuraft.effortless.building.pattern.Patterns;
import dev.huskuraft.effortless.building.replace.Replace;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.replace.ReplaceStrategy;
import dev.huskuraft.effortless.building.settings.Misc;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.builder.Structure;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;
import dev.huskuraft.effortless.screen.wheel.AbstractWheelScreen;

public class EffortlessStructureScreen extends AbstractWheelScreen<Structure, Option> {

    private static final Button<Option> UNDO_OPTION = button(UndoRedo.UNDO);
    private static final Button<Option> REDO_OPTION = button(UndoRedo.REDO);
    private static final Button<Option> SETTING_OPTION = button(Misc.SETTINGS);
    private static final Button<Option> PATTERN_DISABLED_OPTION = button(Patterns.DISABLED, false);
    private static final Button<Option> PATTERN_ENABLED_OPTION = button(Patterns.ENABLED, true);
    private static final Button<Option> PATTERN_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        if (context.pattern().enabled()) {
            var name = Patterns.ENABLED.getNameText().append(" " + context.pattern().transformers().size() + " Transformers");
            var descriptions = new ArrayList<Text>();
            if (!context.pattern().transformers().isEmpty()) {
                descriptions.add(Text.empty());
            }
            for (var transformer : context.pattern().transformers()) {
                descriptions.add(Text.text("").append(transformer.getName().withStyle(ChatFormatting.GRAY)).append("").withStyle(ChatFormatting.GRAY));
                for (var description : transformer.getDescriptions()) {
                    descriptions.add(Text.text(" ").append(description.withStyle(ChatFormatting.DARK_GRAY)));
                }
            }
            return button(Patterns.ENABLED, name, descriptions, true);
        } else {
            return PATTERN_DISABLED_OPTION;
        }
    });

    private static final Button<Option> REPLACE_STRATEGY_DISABLED_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(ReplaceStrategy.DISABLED, context.replaceStrategy() == ReplaceStrategy.DISABLED);
    });
    private static final Button<Option> REPLACE_STRATEGY_BLOCKS_AND_AIR_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(ReplaceStrategy.BLOCKS_AND_AIR, context.replaceStrategy() == ReplaceStrategy.BLOCKS_AND_AIR);
    });
    private static final Button<Option> REPLACE_STRATEGY_BLOCKS_ONLY_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(ReplaceStrategy.BLOCKS_ONLY, context.replaceStrategy() == ReplaceStrategy.BLOCKS_ONLY);
    });
    private static final Button<Option> REPLACE_STRATEGY_OFFHAND_ONLY_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(ReplaceStrategy.OFFHAND_ONLY, context.replaceStrategy() == ReplaceStrategy.OFFHAND_ONLY);
    });
    private static final Button<Option> REPLACE_MODEL_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        if (context.replace().isQuick()) {
            return button(ReplaceMode.QUICK, true);
        } else {
            return button(ReplaceMode.NORMAL, false);
        }
    });
//    private static final Button<Option> REPLACE_CUSTOM_LIST_ONLY_OPTION = button(ReplaceStrategy.CUSTOM_LIST_ONLY, true);

    private static final Button<Option> REPLACE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(context.replace(), context.replace().replaceStrategy() != ReplaceStrategy.DISABLED || context.replace().isQuick());
    });


    private static final Button<Option> PASSIVE_MODE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var builderConfig = entrance.getConfigStorage().get().builderConfig();
        return button(builderConfig.passiveMode() ? PassiveMode.ENABLED : PassiveMode.DISABLED, builderConfig.passiveMode());
    });

    private static final Button<Option> CLIPBOARD_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        return button(context.clipboard(), context.clipboard().enabled());
    });

    private static final Button<Option> GO_BACK_OPTION = button(Misc.GO_BACK, false);


    private final Key assignedKey;

    private AbstractWidget passiveModeTextWidget;

    public EffortlessStructureScreen(Entrance entrance, Key assignedKey) {
        super(entrance, Text.translate("effortless.building.radial.title"));
        this.assignedKey = assignedKey;
    }

    public static Slot<Structure> slot(Structure structure) {
        return slot(
                structure.getMode(),
                structure.getMode().getDisplayName(),
                structure.getMode().getIcon(),
                structure.getMode().getTintColor(),
                structure);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return super.getEntrance();
    }

    protected Player getPlayer() {
        return getEntrance().getClient().getPlayer();
    }

    @Override
    public Key getAssignedKey() {
        return assignedKey;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setRadialSelectResponder((slot, click) -> {
            getEntrance().getStructureBuilder().setStructure(getPlayer(), slot.getContent());
        });
        setRadialOptionSelectResponder((entry, click) -> {
            if (entry.getContent() instanceof Misc misc) {
                switch (misc) {
                    case SETTINGS -> {
                        detach();
                        new EffortlessSettingsScreen(getEntrance()).attach();
                    }
                    case PATTERN -> {
                        detach();
                        new EffortlessPatternScreen(getEntrance()).attach();
                    }
                    case REPLACE -> {
                        setReplaceLeftButtons();
                    }
                    case GO_BACK -> {
                        setLeftButtons();
                    }
                }
                return;
            }
            if (entry.getContent() instanceof Patterns patterns) {
//                if (click) {
//                    var pattern = getEntrance().getStructureBuilder().getContext(getPlayer()).pattern();
//                    getEntrance().getStructureBuilder().setPattern(getPlayer(), pattern.withEnabled(!pattern.enabled()));
//                    return;
//                }
                if (getEntrance().getStructureBuilder().checkPermission(getPlayer())) {
                    detach();
                    new EffortlessPatternScreen(getEntrance()).attach();
                }
                return;
            }
            if (entry.getContent() instanceof Clipboard clipboard) {
                getEntrance().getStructureBuilder().setClipboard(getPlayer(), clipboard.withEnabled(!clipboard.enabled()));
                return;

            }
            if (entry.getContent() instanceof UndoRedo undoRedo) {
                switch (undoRedo) {
                    case UNDO -> {
                        getEntrance().getStructureBuilder().undo(getPlayer());
                    }
                    case REDO -> {
                        getEntrance().getStructureBuilder().redo(getPlayer());
                    }
                }
                return;
            }
            if (entry.getContent() instanceof Replace replace) {
                setReplaceLeftButtons();
                return;
            }
            if (entry.getContent() instanceof ReplaceMode replaceMode) {
                getEntrance().getStructureBuilder().setReplace(getPlayer(), getEntrance().getStructureBuilder().getContext(getPlayer()).replace().withReplaceMode(replaceMode.next()));
                return;
            }
            if (entry.getContent() instanceof ReplaceStrategy replaceStrategy) {
                getEntrance().getStructureBuilder().setReplace(getPlayer(), getEntrance().getStructureBuilder().getContext(getPlayer()).replace().withReplaceStrategy(replaceStrategy));
                return;
            }
            if (entry.getContent() instanceof PassiveMode passiveMode) {
                getEntrance().getConfigStorage().update(config -> config.withPassiveMode(passiveMode != PassiveMode.ENABLED));
                return;
            }
            if (entry.getContent() instanceof BuildFeature buildFeature) {
                var structure = getEntrance().getStructureBuilder().getContext(getPlayer()).structure().withFeature(buildFeature);
                if (getEntrance().getStructureBuilder().setStructure(getPlayer(), structure)) {
                    getEntrance().getConfigStorage().setStructure(structure);
                }
                return;
            }
        });

        this.passiveModeTextWidget = addWidget(new TextWidget(getEntrance(), getX() + getWidth() - 10, getY() + getHeight() - 18, Text.translate("effortless.option.passive_mode"), TextWidget.Gravity.END));

        setLeftButtons();
    }

    private void setLeftButtons() {
        setLeftButtons(
                buttonSet(REPLACE_OPTION, REDO_OPTION, UNDO_OPTION),
                buttonSet(CLIPBOARD_OPTION, PATTERN_OPTION, SETTING_OPTION)
        );
    }

    private void setReplaceLeftButtons() {
        setLeftButtons(
                buttonSet(GO_BACK_OPTION, REPLACE_MODEL_OPTION),
                buttonSet(REPLACE_STRATEGY_DISABLED_OPTION, REPLACE_STRATEGY_BLOCKS_AND_AIR_OPTION, REPLACE_STRATEGY_BLOCKS_ONLY_OPTION, REPLACE_STRATEGY_OFFHAND_ONLY_OPTION)
        );
    }

    @Override
    public void onReload() {
        passiveModeTextWidget.setVisible(getEntrance().getConfigStorage().get().builderConfig().passiveMode());

        setRadialSlots(getEntrance().getConfigStorage().get().structureMap().values().stream().map(EffortlessStructureScreen::slot).toList());

        var structure = getEntrance().getStructureBuilder().getContext(getPlayer()).structure();
//        var structure = getEntrance().getStructureBuilder().getContext(getPlayer()).structure();
        setSelectedSlots(slot(structure));
        setRightButtons(
                structure.getSupportedFeatures().stream().map(feature -> buttonSet(Arrays.stream(feature.getEntries()).map((Feature option) -> button((Option) option, structure.getFeatures().contains(option))).toList())).toList()
        );
    }

}

