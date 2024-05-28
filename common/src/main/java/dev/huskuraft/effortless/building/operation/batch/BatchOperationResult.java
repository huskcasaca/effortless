package dev.huskuraft.effortless.building.operation.batch;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationTooltip;

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
    public OperationTooltip getTooltip() {
        return new OperationTooltip(
                OperationTooltip.Type.BUILD,
                getOperation().getContext(),
                getResults().stream().map(OperationResult::getTooltip).map(OperationTooltip::blockSummary).map(Map::entrySet).flatMap(Set::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BatchOperationResult::merge, LinkedHashMap::new)),
                getResults().stream().map(OperationResult::getTooltip).map(OperationTooltip::entitySummary).map(Map::entrySet).flatMap(Set::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BatchOperationResult::merge, LinkedHashMap::new))
        );
    }

}
