package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.Option;

public enum Settings implements Option {
    GENERAL("settings"),
    MODE_SETTINGS("mode_settings"),
    PATTERN_SETTINGS("pattern_settings"),
    ;

    private final String name;

    Settings(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "misc";
    }

}
