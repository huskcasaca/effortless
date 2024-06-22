package dev.huskuraft.effortless.api.core;

public enum InteractionResult {
    SUCCESS,
    SUCCESS_NO_ITEM_USED,
    CONSUME,
    CONSUME_PARTIAL,
    PASS,
    FAIL;

    public boolean consumesAction() {
        return this == SUCCESS || this == SUCCESS_NO_ITEM_USED ||  this == CONSUME || this == CONSUME_PARTIAL;
    }

}
