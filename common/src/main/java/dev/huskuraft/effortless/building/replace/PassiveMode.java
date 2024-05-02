package dev.huskuraft.effortless.building.replace;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum PassiveMode implements SingleSelectFeature {
    DISABLED("passive_disabled"),
    ENABLED("passive_enabled");

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
        return "passive";
    }


}
