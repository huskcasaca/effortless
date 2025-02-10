package dev.huskuraft.effortless.building.operation.block;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.operation.OperationResult;

public abstract class BlockOperationResult extends OperationResult {

    protected final BlockOperation operation;
    protected final BlockOperationResultType result;
    protected final BlockState blockStateBeforeOp;
    protected final BlockState blockStateAfterOp;
    protected final RecordTag entityTagBeforeOp;
    protected final RecordTag entityTagAfterOp;

    protected BlockOperationResult(
            BlockOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp,
            RecordTag entityTagBeforeOp,
            RecordTag entityTagAfterOp) {
        this.operation = operation;
        this.result = result;
        this.blockStateBeforeOp = blockStateBeforeOp;
        this.blockStateAfterOp = blockStateAfterOp;
        this.entityTagBeforeOp = entityTagBeforeOp;
        this.entityTagAfterOp = entityTagAfterOp;
    }

    protected BlockOperationResult(
            BlockOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp) {
        this.operation = operation;
        this.result = result;
        this.blockStateBeforeOp = blockStateBeforeOp;
        this.blockStateAfterOp = blockStateAfterOp;
        this.entityTagBeforeOp = null;
        this.entityTagAfterOp = null;
    }

    @Override
    public BlockOperation getOperation() {
        return operation;
    }

    @Nullable
    public final BlockState getBlockStateToBreak() {
        return blockStateBeforeOp;
    }

    @Nullable
    public final BlockState getBlockStatePlaced() {
        return blockStateAfterOp;
    }

    @Nullable
    public final BlockState getBlockStateToPlace() {
        return getOperation().getBlockState();
    }

    @Nullable
    public RecordTag getEntityTagToBreak() {
        return entityTagBeforeOp;
    }

    @Nullable
    public RecordTag getEntityTagPlaced() {
        return entityTagAfterOp;
    }

    @Nullable
    public RecordTag getEntityTagToPlace() {
        return getOperation().getEntityTag();
    }

    @Nullable
    public BlockEntity getBlockEntityToBreak() {
        return getBlockEntity(getBlockStateToBreak(), getEntityTagToBreak());
    }

    @Nullable
    public BlockEntity getBlockEntityPlaced() {
        return getBlockEntity(getBlockStatePlaced(), getEntityTagPlaced());
    }

    @Nullable
    public BlockEntity getBlockEntityToPlace() {
        return getBlockEntity(getBlockStateToPlace(), getEntityTagToPlace());
    }

    private BlockEntity getBlockEntity(BlockState blockState, RecordTag entityTag) {
        if (blockState == null) {
            return null;
        }
        var blockEntity = blockState.getEntity(getOperation().getBlockPosition());
        if (blockEntity == null) {
            return null;
        }
        if (entityTag != null) {
            try {
                blockEntity.setTag(entityTag);
            } catch (Exception e) {
                Effortless.LOGGER.warn("Fail to set block entity tag for: {}", blockEntity.refs());
            }
        }
        return blockEntity;
    }

    @Nullable
    public final BlockState getBlockStateForRenderer() {
        if (getBlockStateToBreak() == null || getBlockStateToPlace() == null) {
            return null;
        }
        if (!getBlockStateToBreak().isAir() && getBlockStateToPlace().isAir()) {
            return getBlockStateToBreak();
        } else {
            return getBlockStateToPlace();
        }
    }

    @Nullable
    public final BlockEntity getBlockEntityForRenderer() {
        return getBlockEntityToPlace();
    }

    @Override
    public int getAffectedBlockCount() {
        return result().success() ? 1 : 0;
    }

    public BlockOperationResultType result() {
        return result;
    }

}
