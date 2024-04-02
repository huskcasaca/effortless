package dev.huskuraft.effortless.building.operation.batch;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public Color getColor() {
        return null;
    }

    public Collection<? extends OperationResult> getResult() {
        return result;
    }

    public List<BlockPosition> locations() {
        return result.stream().map(OperationResult::getOperation).filter(BlockPositionLocatable.class::isInstance).map(BlockPositionLocatable.class::cast).map(BlockPositionLocatable::locate).filter(Objects::nonNull).toList();
    }

    public Map<Color, List<OperationResult>> getResultByColor() {
        return result.stream().filter(result -> Objects.nonNull(result.getColor())).collect(Collectors.groupingBy(OperationResult::getColor));
    }

}
