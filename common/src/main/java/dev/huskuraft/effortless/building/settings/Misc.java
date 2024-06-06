package dev.huskuraft.effortless.building.settings;

import dev.huskuraft.effortless.building.Option;

public enum Misc implements Option {
    SETTINGS("settings"),
    PATTERN("pattern"),
    GO_BACK("go_back"),
    ;

    private final String name;

    Misc(String name) {
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
