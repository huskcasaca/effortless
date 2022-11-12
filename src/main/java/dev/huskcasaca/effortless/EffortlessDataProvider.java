package dev.huskcasaca.effortless;

import dev.huskcasaca.effortless.buildconfig.ReachSettingsManager;
import dev.huskcasaca.effortless.buildmode.ModeSettingsManager;
import dev.huskcasaca.effortless.buildmodifier.ModifierSettingsManager;


public interface EffortlessDataProvider {

    ModeSettingsManager.ModeSettings getModeSettings();

    void setModeSettings(ModeSettingsManager.ModeSettings modeSettings);

    ModifierSettingsManager.ModifierSettings getModifierSettings();

    void setModifierSettings(ModifierSettingsManager.ModifierSettings modifierSettings);

    ReachSettingsManager.ReachSettings getReachSettings();

    void setReachSettings(ReachSettingsManager.ReachSettings reachSettings);

}
