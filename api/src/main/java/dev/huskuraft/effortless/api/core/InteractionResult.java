package dev.huskuraft.effortless.api.core;

public enum InteractionResult {
    SUCCESS,
    CONSUME,
    CONSUME_PARTIAL,
    PASS,
    FAIL;

    public boolean consumesAction() {
        return this == SUCCESS || this == CONSUME || this == CONSUME_PARTIAL;
    }

    public boolean shouldSwing() {
        return this == SUCCESS;
    }

    public boolean shouldAwardStats() {
        return this == SUCCESS || this == CONSUME;
    }

    public static InteractionResult sidedSuccess(boolean isClientSide) {
        return isClientSide ? SUCCESS : CONSUME;
    }
}
