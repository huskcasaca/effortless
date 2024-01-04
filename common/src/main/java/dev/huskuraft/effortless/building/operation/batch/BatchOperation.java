package dev.huskuraft.effortless.building.operation.batch;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.TransformableOperation;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public abstract class BatchOperation extends TransformableOperation {

    protected final Context context;
    protected final Supplier<Stream<? extends TransformableOperation>> operationsSupplier;

    protected BatchOperation(Context context, Supplier<Stream<? extends TransformableOperation>> operationsSupplier) {
        this.context = context;
        this.operationsSupplier = operationsSupplier;
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

    public Stream<? extends TransformableOperation> operations() {
        return operationsSupplier.get();
    }

    @Override
    public BlockPosition locate() {
        return null;
    }
}
