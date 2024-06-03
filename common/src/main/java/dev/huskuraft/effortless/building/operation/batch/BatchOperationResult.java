package dev.huskuraft.effortless.building.operation.batch;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
import dev.huskuraft.effortless.building.operation.OperationResult;

public class BatchOperationResult extends OperationResult {

    private final BatchOperation operation;
    private final Collection<? extends OperationResult> result;

    BatchOperationResult(BatchOperation operation, Collection<? extends OperationResult> result) {
        this.operation = operation;
        this.result = result;
    }

    private static <K> Map<K, Integer> merge(Map<K, Integer> a, Map<K, Integer> b) {
        return merge(a, b, Integer::sum);
    }

    private static <K, V> Map<K, V> merge(Map<K, V> a, Map<K, V> b, BiFunction<V, V, V> merge) {
        for (var entry : b.entrySet()) {
            a.compute(entry.getKey(), (k, v) -> v == null ? entry.getValue() : merge.apply(v, entry.getValue()));
        }
        return a;
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

    public Collection<? extends OperationResult> getResults() {
        return result;
    }

    @Override
    public List<BlockState> getBlockStateSummary(BlockStateSummary blockStateSummary) {
        return result.stream().map(result -> result.getBlockStateSummary(blockStateSummary)).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary) {
        return result.stream().map(result -> result.getBlockEntitySummary(blockEntitySummary)).flatMap(List::stream).collect(Collectors.toList());
    }

}
