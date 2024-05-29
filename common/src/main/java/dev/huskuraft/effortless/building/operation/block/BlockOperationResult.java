package dev.huskuraft.effortless.building.operation.block;

import javax.annotation.Nullable;

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

    @Nullable
    public final BlockState getBlockStateBeforeOp() {
        return blockStateBeforeOp;
    }

    @Nullable
    public final BlockState getBlockStateAfterOp() {
        return blockStateAfterOp;
    }

    @Nullable
    public final BlockState getBlockStateInOp() {
        return getOperation().getBlockState();
    }

    @Nullable
    public final BlockState getBlockStateForRenderer() {
        if (getBlockStateBeforeOp() == null || getBlockStateInOp() == null) {
            return null;
        }
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
