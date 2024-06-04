package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.clipboard.BlockSnapshot;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockStateCopyOperationResult extends BlockOperationResult {

    public BlockStateCopyOperationResult(
            BlockStateCopyOperation operation,
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

    public BlockPosition getRelativePosition() {
        return getOperation().getInteraction().blockPosition().sub(getOperation().getContext().getInteractions().get(0).blockPosition());
    }

    public BlockSnapshot getBlockSnapshot() {
        if (getOperation().getBlockState().isAir()) {
            return new BlockSnapshot(getRelativePosition(), null, null);
        }
        return new BlockSnapshot(getRelativePosition(), getOperation().getBlockState(), getOperation().getEntityTag());
    }

    @Override
    public List<BlockState> getBlockStateSummary(BlockStateSummary blockStateSummary) {
        var blockState = switch (blockStateSummary) {
            case BLOCKS_COPIED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NOT_COPYABLE -> switch (result) {
                case FAIL_BREAK_REPLACE_RULE, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_COPY_BLACKLISTED -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_COPY_NO_PERMISSION -> getBlockStateToBreak();
                default -> null;
            };
            default -> null;
        };
        if (blockState == null) {
            return List.of();
        }
        return List.of(blockState);
    }

    @Override
    public List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary) {
        return List.of();
    }

}
