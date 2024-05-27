package dev.huskuraft.effortless.building.clipboard;

import dev.huskuraft.effortless.api.lang.Lang;
import dev.huskuraft.effortless.building.Option;

public enum Clipboards implements Option {
    DISABLED("clipboard_disabled"),
    ENABLED("clipboard_enabled")
    ;

    private final String name;

    Clipboards(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "clipboard";
    }

    @Override
    public String getTooltipKey() {
        return Lang.asKey("option.%s.tooltip".formatted(getCategory()));
    }
}
