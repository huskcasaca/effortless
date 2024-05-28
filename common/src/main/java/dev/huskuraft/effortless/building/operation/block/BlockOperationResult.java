package dev.huskuraft.effortless.building.operation.block;

import java.util.List;
import java.util.Map;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.operation.BlockSummary;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationTooltip;

public abstract class BlockOperationResult extends OperationResult {

    protected final BlockOperation operation;
    protected final Type result;
    protected final BlockState oldBlockState;
    protected final BlockState newBlockState;

    protected BlockOperationResult(
            BlockOperation operation,
            Type result,
            BlockState oldBlockState,
            BlockState newBlockState) {
        this.operation = operation;
        this.result = result;
        this.oldBlockState = oldBlockState;
        this.newBlockState = newBlockState;
    }

    @Override
    public BlockOperation getOperation() {
        return operation;
    }

    public final BlockState getOldBlockState() {
        return oldBlockState;
    }

    public final BlockState getNewBlockState() {
        return newBlockState;
    }

    @Override
    public final OperationTooltip getTooltip() {
        return OperationTooltip.build(getOperation().getContext(), getBlockSummary(), Map.of());
    }

    public abstract Map<BlockSummary, List<BlockState>> getBlockSummary();

    public Type result() {
        return result;
    }

    public enum Type {
        SUCCESS,
        SUCCESS_PARTIAL,
        CONSUME,

        FAIL_WORLD_HEIGHT,
        FAIL_WORLD_BORDER,
        FAIL_WORLD_INCORRECT_DIM,
        FAIL_PLAYER_GAME_MODE,

        FAIL_PLACE_ITEM_INSUFFICIENT,
        FAIL_PLACE_ITEM_NOT_BLOCK,
        FAIL_BREAK_TOOL_INSUFFICIENT,
        FAIL_BREAK_REPLACE_RULE,

        FAIL_CONFIG_BREAK_BLACKLISTED,
        FAIL_CONFIG_PLACE_BLACKLISTED,
        FAIL_CONFIG_INTERACT_BLACKLISTED,
        FAIL_CONFIG_COPY_BLACKLISTED,

        FAIL_BREAK_NO_PERMISSION,
        FAIL_PLACE_NO_PERMISSION,
        FAIL_INTERACT_NO_PERMISSION,
        FAIL_COPY_NO_PERMISSION,

        FAIL_BLOCK_STATE_NULL,
        FAIL_BLOCK_STATE_AIR,
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
