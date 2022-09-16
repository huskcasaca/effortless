package dev.huskuraft.effortless.building.operation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public interface OperationFilter<O> extends Predicate<O> {

    static <O extends BlockPositionLocatable> OperationFilter<O> distinctByLocation() {
        return distinctBy(BlockPositionLocatable::locate);
    }

    private static <O> OperationFilter<O> distinctBy(Function<O, ?> propertyGetter) {
        var seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(propertyGetter.apply(t));
    }

}
