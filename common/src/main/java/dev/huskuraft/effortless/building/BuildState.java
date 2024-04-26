package dev.huskuraft.effortless.building;

public enum BuildState {
    IDLE,
    PLACE_BLOCK,
    BREAK_BLOCK,
    INTERACT_BLOCK
    ;

    public boolean isIdle() {
        return this == IDLE;
    }

}
