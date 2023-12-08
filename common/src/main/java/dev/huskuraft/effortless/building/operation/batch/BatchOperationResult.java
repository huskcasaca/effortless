package dev.huskuraft.effortless.building.operation.batch;

import dev.huskuraft.effortless.building.operation.BlockPositionLocatable;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BatchOperationResult extends OperationResult {

    private final BatchOperation operation;
    private final Collection<? extends OperationResult> result;

    BatchOperationResult(BatchOperation operation, Collection<? extends OperationResult> result) {
        this.operation = operation;
        this.result = result;
    }


    @Override
    public BatchOperation getOperation() {
        return operation;
    }

    @Override
    public BatchOperation getReverseOperation() {
        return null;
    }

    @Override
    public Collection<ItemStack> get(ItemType type) {
        return ItemStackUtils.reduceStack(result.stream().map(result -> result.get(type)).flatMap(Collection::stream).toList());
    }

    public Collection<? extends OperationResult> getResult() {
        return result;
    }

    public List<BlockPosition> locations() {
        return result.stream().map(result -> result.getOperation() instanceof BlockPositionLocatable blockPositionLocatable ? blockPositionLocatable.locate() : null).filter(Objects::nonNull).toList();

    }

}
