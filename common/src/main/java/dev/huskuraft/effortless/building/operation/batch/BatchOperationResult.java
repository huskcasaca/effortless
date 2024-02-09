package dev.huskuraft.effortless.building.operation.batch;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.BlockPositionLocatable;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.OperationResult;

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
        return new DeferredBatchOperation(
                operation.getContext(),
                () -> result.stream().map(OperationResult::getReverseOperation)
        );
    }

    @Override
    public Collection<ItemStack> getProducts(ItemType type) {
        return ItemStackUtils.reduceStack(result.stream().map(result -> result.getProducts(type)).flatMap(Collection::stream).toList());
    }

    public Collection<? extends OperationResult> getResult() {
        return result;
    }

    public List<BlockPosition> locations() {
        return result.stream().map(result -> result.getOperation() instanceof BlockPositionLocatable blockPositionLocatable ? blockPositionLocatable.locate() : null).filter(Objects::nonNull).toList();

    }

}
