package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.Option;

public enum Settings implements Option {
    SETTINGS("settings"),
    PATTERN("pattern"),
    REPLACE("replace"),
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
