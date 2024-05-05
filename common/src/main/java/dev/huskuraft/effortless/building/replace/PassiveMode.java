package dev.huskuraft.effortless.building.replace;

import dev.huskuraft.effortless.EffortlessKeys;
import dev.huskuraft.effortless.api.lang.Lang;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PassiveMode implements SingleSelectFeature {
    DISABLED("passive_mode_disabled"),
    ENABLED("passive_mode_enabled");

    private final String name;

    PassiveMode(String name) {
        this.name = name;
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
    public String getTooltipKey() {
        return Lang.asKey("option.%s.tooltip".formatted(getCategory()));
    }

    @Override
    public Text getTooltipText() {
        return Text.translate(getTooltipKey(), Text.translate(EffortlessKeys.PASSIVE_BUILD_MODIFIER.getBinding().getName()).withStyle(TextStyle.GRAY)).withStyle(TextStyle.DARK_GRAY);
    }
}
