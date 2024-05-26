package dev.huskuraft.effortless.building.replace;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum ReplaceMode implements SingleSelectFeature {
    DISABLED("replace_disabled"),
    BLOCKS_AND_AIR("replace_blocks_and_air"),
    BLOCKS_ONLY("replace_blocks_only"),
    OFFHAND_ONLY("replace_offhand_only"),
    CUSTOM_LIST_ONLY("replace_custom");

    private final String name;

    ReplaceMode(String name) {
        this.name = name;
    }

    public ReplaceMode next() {
        return switch (this) {
            case DISABLED -> BLOCKS_AND_AIR;
            case BLOCKS_AND_AIR -> BLOCKS_ONLY;
            case BLOCKS_ONLY -> OFFHAND_ONLY;
            case OFFHAND_ONLY -> CUSTOM_LIST_ONLY;
            case CUSTOM_LIST_ONLY -> DISABLED;
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

}
