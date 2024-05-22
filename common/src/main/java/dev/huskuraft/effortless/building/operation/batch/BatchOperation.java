package dev.huskuraft.effortless.building.operation.batch;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.TransformableOperation;

public abstract class BatchOperation extends TransformableOperation {

    protected final Context context;

    protected BatchOperation(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public abstract BatchOperationResult commit();

    public abstract BatchOperation map(UnaryOperator<TransformableOperation> operator);

    public abstract BatchOperation mapEach(UnaryOperator<TransformableOperation> operator);

    public abstract BatchOperation flatten();

    public abstract BatchOperation filter(Predicate<TransformableOperation> predicate);

    public abstract Stream<? extends TransformableOperation> operations();

    @Override
    public BlockPosition locate() {
        return null;
    }
}
