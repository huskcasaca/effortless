package dev.huskuraft.effortless.building.replace;

import dev.huskuraft.effortless.EffortlessKeys;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PassiveMode implements SingleSelectFeature {
    DISABLED("passive_mode_disabled"),
    ENABLED("passive_mode_enabled");

    private final String name;

    PassiveMode(String name) {
        this.name = name;
    }


    public PassiveMode next() {
        return switch (this) {
            case DISABLED -> ENABLED;
            case ENABLED -> DISABLED;
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "passive_mode";
    }

    @Override
    public Text getTooltipText() {
        return Text.translate(getTooltipKey(), EffortlessKeys.PASSIVE_BUILD_MODIFIER);
    }
}
