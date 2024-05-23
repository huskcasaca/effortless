package dev.huskuraft.effortless.building.operation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.huskuraft.effortless.building.operation.block.BlockOperation;

public interface OperationFilter<O> extends Predicate<O> {

    static <O extends Operation> OperationFilter<O> distinctBlockOperations() {
        return distinctBy(operation -> operation instanceof BlockOperation blockOperation ? blockOperation.getBlockPosition() : null);
    }

    static <O extends Operation> OperationFilter<O> distinctBy(Function<O, ?> propertyGetter) {
        var seen = ConcurrentHashMap.newKeySet();
        return t -> {
            var property = propertyGetter.apply(t);
            return property != null && seen.add(propertyGetter.apply(t));
        };
    }

}
