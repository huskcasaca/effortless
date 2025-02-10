package dev.huskuraft.effortless.screen.structure;

import java.util.ArrayList;
import java.util.Arrays;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.input.KeyBinding;
import dev.huskuraft.universal.api.input.OptionKeys;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.Feature;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.config.PassiveMode;
import dev.huskuraft.effortless.building.history.UndoRedo;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.Replace;
import dev.huskuraft.effortless.building.replace.ReplaceStrategy;
import dev.huskuraft.effortless.building.settings.Misc;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.builder.Structure;
import dev.huskuraft.effortless.screen.clipboard.EffortlessClipboardScreen;
import dev.huskuraft.effortless.screen.pattern.EffortlessPatternScreen;
import dev.huskuraft.effortless.screen.settings.EffortlessSettingsScreen;
import dev.huskuraft.effortless.screen.wheel.AbstractWheelScreen;

public class EffortlessStructureScreen extends AbstractWheelScreen<Structure, Option> {

    private static final Button<Option> UNDO_OPTION = button(UndoRedo.UNDO);
    private static final Button<Option> REDO_OPTION = button(UndoRedo.REDO);
    private static final Button<Option> SETTING_OPTION = button(Misc.SETTINGS);
    private static final Button<Option> PATTERN_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        var descriptions = new ArrayList<Text>();
        var name = context.pattern().getNameText();
        if (context.pattern().enabled()) {
//            name = context.pattern().getNameText().append(" " + context.pattern().transformers().size() + " Transformers");
            if (!context.pattern().transformers().isEmpty()) {
                descriptions.add(Text.empty());
            }
            for (var transformer : context.pattern().transformers()) {
                descriptions.add(Text.text("").append(transformer.getName().withStyle(ChatFormatting.GRAY)).append("").withStyle(ChatFormatting.GRAY));
                for (var description : transformer.getDescriptions()) {
                    descriptions.add(Text.text(" ").append(description.withStyle(ChatFormatting.DARK_GRAY)));
                }
            }
        }
        descriptions.add(Text.empty());
        descriptions.add(Text.translate("effortless.tooltip.click_to_toggle_on_off", OptionKeys.KEY_ATTACK.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));
        descriptions.add(Text.translate("effortless.tooltip.click_to_edit_pattern", OptionKeys.KEY_USE.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));
        return button(context.pattern(), context.pattern().enabled(), name, descriptions);
    });

//    private static final Button<Option> REPLACE_STRATEGY_DISABLED_OPTION = lazyButton(() -> {
//        var entrance = EffortlessClient.getInstance();
//        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
//        return button(ReplaceStrategy.DISABLED, context.replaceStrategy() == ReplaceStrategy.DISABLED);
//    });
//    private static final Button<Option> REPLACE_STRATEGY_BLOCKS_AND_AIR_OPTION = lazyButton(() -> {
//        var entrance = EffortlessClient.getInstance();
//        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
//        return button(ReplaceStrategy.BLOCKS_AND_AIR, context.replaceStrategy() == ReplaceStrategy.BLOCKS_AND_AIR);
//    });
//    private static final Button<Option> REPLACE_STRATEGY_BLOCKS_ONLY_OPTION = lazyButton(() -> {
//        var entrance = EffortlessClient.getInstance();
//        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
//        return button(ReplaceStrategy.BLOCKS_ONLY, context.replaceStrategy() == ReplaceStrategy.BLOCKS_ONLY);
//    });
//    private static final Button<Option> REPLACE_STRATEGY_OFFHAND_ONLY_OPTION = lazyButton(() -> {
//        var entrance = EffortlessClient.getInstance();
//        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
//        return button(ReplaceStrategy.OFFHAND_ONLY, context.replaceStrategy() == ReplaceStrategy.OFFHAND_ONLY);
//    });
//    private static final Button<Option> REPLACE_MODEL_OPTION = lazyButton(() -> {
//        var entrance = EffortlessClient.getInstance();
//        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
//        if (context.replace().isQuick()) {
//            return button(ReplaceMode.QUICK, true);
//        } else {
//            return button(ReplaceMode.NORMAL, false);
//        }
//    });
//    private static final Button<Option> REPLACE_CUSTOM_LIST_ONLY_OPTION = button(ReplaceStrategy.CUSTOM_LIST_ONLY, true);

    private static final Button<Option> REPLACE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());

        var name = context.replace().getNameText();

        var descriptions = new ArrayList<Text>();

        descriptions.add(Text.empty());
        descriptions.add(Text.translate("effortless.tooltip.click_to_switch_replace_strategy", OptionKeys.KEY_ATTACK.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));
        descriptions.add(Text.translate("effortless.tooltip.click_to_toggle_quick_replace", OptionKeys.KEY_USE.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));

        return button(context.replace(), context.replace().replaceStrategy() != ReplaceStrategy.DISABLED || context.replace().isQuick(), name, descriptions);
    });


    private static final Button<Option> PASSIVE_MODE_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var builderConfig = entrance.getConfigStorage().get().builderConfig();
        return button(builderConfig.passiveMode() ? PassiveMode.ENABLED : PassiveMode.DISABLED, builderConfig.passiveMode());
    });

    private static final Button<Option> CLIPBOARD_OPTION = lazyButton(() -> {
        var entrance = EffortlessClient.getInstance();
        var context = entrance.getStructureBuilder().getContext(entrance.getClient().getPlayer());
        var descriptions = new ArrayList<Text>();
        descriptions.add(Text.empty());
        descriptions.add(Text.translate("effortless.tooltip.click_to_toggle_on_off", OptionKeys.KEY_ATTACK.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));
        descriptions.add(Text.translate("effortless.tooltip.click_to_edit_clipboard", OptionKeys.KEY_USE.getKeyBinding().getKey().getNameText()).withStyle(ChatFormatting.DARK_GRAY));
        return button(context.clipboard(), context.clipboard().enabled(), context.clipboard().getNameText(), descriptions);
    });

    private static final Button<Option> GO_BACK_OPTION = button(Misc.GO_BACK, false);


    private final KeyBinding assignedKey;

    private AbstractWidget passiveModeTextWidget;

    public EffortlessStructureScreen(Entrance entrance, KeyBinding assignedKey) {
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
    public KeyBinding getAssignedKeyBinds() {
        return assignedKey;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setRadialSelectResponder((slot, click) -> {
            setScaleAnimation(3f);
            getEntrance().getStructureBuilder().setStructure(getPlayer(), slot.getContent());
        });
        setRadialOptionSelectResponder((entry, click) -> {
            setScaleAnimation(3f);
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
                    case GO_BACK -> {
                        setLeftButtons();
                    }
                }
                return;
            }
            if (entry.getContent() instanceof Pattern pattern) {
                if (click) {
                    getEntrance().getStructureBuilder().setPattern(getPlayer(), pattern.toggled());
                    return;
                }
                if (getEntrance().getStructureBuilder().checkPermission(getPlayer())) {
                    detach();
                    new EffortlessPatternScreen(getEntrance()).attach();
                }
                return;
            }
            if (entry.getContent() instanceof Clipboard clipboard) {
                if (click) {
                    getEntrance().getStructureBuilder().setClipboard(getPlayer(), clipboard.toggled());
                    return;
                }
                if (getEntrance().getStructureBuilder().checkPermission(getPlayer())) {
                    detach();
                    new EffortlessClipboardScreen(getEntrance()).attach();
                }
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
                if (click) {
                    getEntrance().getStructureBuilder().setReplace(getPlayer(), getEntrance().getStructureBuilder().getContext(getPlayer()).replace().withReplaceStrategy(getEntrance().getStructureBuilder().getContext(getPlayer()).replace().replaceStrategy().next()));
                    return;
                }
                getEntrance().getStructureBuilder().setReplace(getPlayer(), getEntrance().getStructureBuilder().getContext(getPlayer()).replace().withQuick(!getEntrance().getStructureBuilder().getContext(getPlayer()).replace().isQuick()));

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

//    private void setReplaceLeftButtons() {
//        setLeftButtons(
//                buttonSet(GO_BACK_OPTION, REPLACE_MODEL_OPTION),
//                buttonSet(REPLACE_STRATEGY_DISABLED_OPTION, REPLACE_STRATEGY_BLOCKS_AND_AIR_OPTION, REPLACE_STRATEGY_BLOCKS_ONLY_OPTION, REPLACE_STRATEGY_OFFHAND_ONLY_OPTION)
//        );
//    }

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

