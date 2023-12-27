package dev.huskuraft.effortless.building.replace;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum ReplaceMode implements SingleSelectFeature {
    DISABLED("replace_disabled"),
    NORMAL("replace_normal"),
    QUICK("replace_quick");

    private final String name;

    ReplaceMode(String name) {
        this.name = name;
    }

    public String getCommandName() {
        return switch (this) {
            case DISABLED -> "disabled";
            case NORMAL -> "normal";
            case QUICK -> "quick";
        };
    }

    public ReplaceMode next() {
        return switch (this) {
            case DISABLED -> NORMAL;
            case NORMAL -> QUICK;
            case QUICK -> DISABLED;
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return "replace";
    }

    public boolean isReplace() {
        return this != DISABLED;
    }

    public boolean isQuick() {
        return this == QUICK;
    }

}
