package dev.huskuraft.effortless.building.operation.batch;

import java.util.Collection;
import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationSummaryType;

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
    public List<ItemStack> getSummary(OperationSummaryType type) {
        return ItemStackUtils.reduceStack(result.stream().map(result -> result.getSummary(type)).flatMap(Collection::stream).toList());
    }


    public Collection<? extends OperationResult> getResult() {
        return result;
    }

//    public List<BlockPosition> locations() {
//        return result.stream().map(OperationResult::getOperation).filter(BlockPositionLocatable.class::isInstance).map(BlockPositionLocatable.class::cast).map(BlockPositionLocatable::locate).filter(Objects::nonNull).toList();
//    }


}
