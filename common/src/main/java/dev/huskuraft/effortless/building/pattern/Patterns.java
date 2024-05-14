package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.lang.Lang;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.Option;

public enum Patterns implements Option {
    DISABLED("pattern_disabled"),
    ENABLED("pattern_enabled")
    ;

    private final String name;

    Patterns(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "pattern";
    }

    @Override
    public String getTooltipKey() {
        return Lang.asKey("option.%s.tooltip".formatted(getCategory()));
    }

    @Override
    public Text getTooltipText() {
        return Text.translate(getTooltipKey(), Text.translate("%s.build_transformers".formatted(getTooltipKey())).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY);
    }
}
