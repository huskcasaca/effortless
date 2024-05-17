package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.OperationResult;

public abstract class BlockOperationResult extends OperationResult {

    protected final BlockOperation operation;
    protected final Type result;
    protected final List<ItemStack> inputs;
    protected final List<ItemStack> outputs;

    protected BlockOperationResult(
            BlockOperation operation,
            Type result,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        this.operation = operation;
        this.result = result;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public BlockOperation getOperation() {
        return operation;
    }

    public Type result() {
        return result;
    }

    public List<ItemStack> inputs() {
        return inputs;
    }

    public List<ItemStack> outputs() {
        return outputs;
    }

    public enum Type {
        SUCCESS,
        SUCCESS_PARTIAL,
        CONSUME,

        FAIL_WORLD_HEIGHT,
        FAIL_WORLD_BORDER,

        FAIL_PLAYER_IS_SPECTATOR,
        FAIL_PLAYER_CANNOT_ATTACK,
        FAIL_PLAYER_CANNOT_BREAK,
        FAIL_PLAYER_CANNOT_INTERACT,

        FAIL_ITEM_INSUFFICIENT,
        FAIL_ITEM_NOT_BLOCK,
        FAIL_TOOL_INSUFFICIENT,

        FAIL_WHITELISTED,
        FAIL_BLACKLISTED,

        FAIL_BLOCK_STATE_AIR,
        FAIL_BLOCK_STATE_NULL,

        FAIL_UNKNOWN;

        public boolean consumesAction() {
            return this == SUCCESS || this == SUCCESS_PARTIAL || this == CONSUME;
        }

        public boolean success() {
            return this == SUCCESS || this == SUCCESS_PARTIAL || this == CONSUME;
        }

        public boolean fail() {
            return this != SUCCESS && this != SUCCESS_PARTIAL && this != CONSUME;
        }

        public boolean shouldSwing() {
            return this == SUCCESS || this == SUCCESS_PARTIAL;
        }

        public boolean shouldAwardStats() {
            return this == SUCCESS || this == SUCCESS_PARTIAL;
        }
    }
}
