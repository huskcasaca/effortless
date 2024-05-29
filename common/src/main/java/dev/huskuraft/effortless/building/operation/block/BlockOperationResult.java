package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.operation.OperationResult;

public abstract class BlockOperationResult extends OperationResult {

    protected final BlockOperation operation;
    protected final BlockOperationResultType result;
    protected final BlockState blockStateBeforeOp;
    protected final BlockState blockStateAfterOp;

    protected BlockOperationResult(
            BlockOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp) {
        this.operation = operation;
        this.result = result;
        this.blockStateBeforeOp = blockStateBeforeOp;
        this.blockStateAfterOp = blockStateAfterOp;
    }

    @Override
    public BlockOperation getOperation() {
        return operation;
    }

    public final BlockState getBlockStateBeforeOp() {
        return blockStateBeforeOp;
    }

    public final BlockState getBlockStateAfterOp() {
        return blockStateAfterOp;
    }

    public final BlockState getBlockStateInOp() {
        return getOperation().getBlockState();
    }

    public final BlockState getBlockStateForRenderer() {
        if (!getBlockStateBeforeOp().isAir() && getBlockStateInOp().isAir()) {
            return getBlockStateBeforeOp();
        } else {
            return getBlockStateInOp();
        }
    }

    public BlockOperationResultType result() {
        return result;
    }

}
