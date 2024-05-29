package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.operation.BlockSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockInteractOperationResult extends BlockOperationResult {

    public BlockInteractOperationResult(
            BlockInteractOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp
    ) {
        super(operation, result, blockStateBeforeOp, blockStateAfterOp);
    }

    @Override
    public Operation getReverseOperation() {
        return new EmptyOperation(operation.getContext());
    }

    @Override
    public List<BlockState> getBlockSummary(BlockSummary blockSummary) {
        switch (blockSummary) {
            case BLOCKS_INTERACTED -> {
                switch (result) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> {
                        return List.of(getBlockStateBeforeOp());
                    }
                }
            }
            case BLOCKS_NOT_INTERACTABLE -> {
                switch (result) {
                    case FAIL_BREAK_REPLACE_RULE, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> {
                        return List.of(getBlockStateBeforeOp());
                    }
                }
            }
            case BLOCKS_BLACKLISTED -> {
                switch (result) {
                    case FAIL_INTERACT_BLACKLISTED -> {
                        return List.of(getBlockStateBeforeOp());
                    }
                }
            }
            case BLOCKS_NO_PERMISSION -> {
                switch (result) {
                    case FAIL_INTERACT_NO_PERMISSION -> {
                        return List.of(getBlockStateBeforeOp());
                    }
                }
            }
            case BLOCKS_TOOLS_INSUFFICIENT -> {
                switch (result) {
                    case FAIL_BREAK_TOOL_INSUFFICIENT -> {
                        return List.of(getBlockStateBeforeOp());
                    }
                }
            }

        }
        return List.of();
    }


}
