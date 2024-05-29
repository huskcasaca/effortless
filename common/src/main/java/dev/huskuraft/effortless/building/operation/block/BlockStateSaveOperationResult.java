package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.clipboard.BlockSnapshot;
import dev.huskuraft.effortless.building.operation.BlockSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockStateSaveOperationResult extends BlockOperationResult {

    public BlockStateSaveOperationResult(
            BlockStateSaveOperation operation,
            Type result,
            BlockState oldBlockState,
            BlockState newBlockState
    ) {
        super(operation, result, oldBlockState, newBlockState);
    }

    @Override
    public Operation getReverseOperation() {
        return new EmptyOperation();
    }

    public BlockPosition getRelativePosition() {
        return getOperation().getInteraction().blockPosition().sub(getOperation().getContext().getInteractions().get(0).blockPosition());
    }

    public BlockSnapshot getBlockSnapshot() {
        return new BlockSnapshot(getRelativePosition(), getOperation().getBlockState());
    }

    @Override
    public List<BlockState> getBlockSummary(BlockSummary blockSummary) {
        switch (blockSummary) {
            case BLOCKS_COPIED -> {
                switch (result) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> {
                        return List.of(getOriginalBlockState());
                    }
                }
            }
            case BLOCKS_NOT_COPYABLE -> {
                switch (result) {
                    case FAIL_BREAK_REPLACE_RULE, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> {
                        return List.of(getOriginalBlockState());
                    }
                }
            }
            case BLOCKS_BLACKLISTED -> {
                switch (result) {
                    case FAIL_COPY_BLACKLISTED -> {
                        return List.of(getOriginalBlockState());
                    }
                }
            }
            case BLOCKS_NO_PERMISSION -> {
                switch (result) {
                    case FAIL_COPY_NO_PERMISSION -> {
                        return List.of(getOriginalBlockState());
                    }
                }
            }

        }
        return List.of();
    }

}
