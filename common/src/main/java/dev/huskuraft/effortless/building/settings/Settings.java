package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.Option;

public enum Settings implements Option {
    DIMENSION_SETTINGS("dimension_settings"),
    MODE_SETTINGS("mode_settings"),
    PATTERN_SETTINGS("pattern_settings"),
    PROFILE_SETTINGS("profile_settings"),
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
        return "settings";
    }

}
