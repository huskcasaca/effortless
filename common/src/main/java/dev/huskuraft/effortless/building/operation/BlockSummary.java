package dev.huskuraft.effortless.building.operation;

public enum BlockSummary {
    HIDDEN,
    BLOCKS_PLACED,
    BLOCKS_DESTROYED,
    BLOCKS_INTERACTED,
    BLOCKS_COPIED,

    BLOCKS_NOT_PLACEABLE,
    BLOCKS_NOT_BREAKABLE,
    BLOCKS_NOT_INTERACTABLE,
    BLOCKS_NOT_COPYABLE,

    BLOCKS_ITEMS_INSUFFICIENT,
    BLOCKS_TOOLS_INSUFFICIENT,

    BLOCKS_BLACKLISTED,
    BLOCKS_NO_PERMISSION;

    public boolean isSuccess() {
        return this == BLOCKS_PLACED || this == BLOCKS_DESTROYED || this == BLOCKS_INTERACTED;
    }

}
