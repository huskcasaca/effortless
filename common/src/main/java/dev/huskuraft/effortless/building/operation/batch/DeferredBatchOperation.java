package dev.huskuraft.effortless.building.operation.batch;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DeferredBatchOperation extends BatchOperation {

    public DeferredBatchOperation(Context context, Supplier<Stream<? extends TransformableOperation>> operationsSupplier) {
        super(context, operationsSupplier);
    }

    @Override
    public BatchOperationResult commit() {
        return new BatchOperationResult(this, operations().map(Operation::commit).toList());
    }

    @Override
    public DeferredBatchOperation move(MoveContext moveContext) {
        return new DeferredBatchOperation(context, () -> operations().map(o -> o.move(moveContext)));
    }

    @Override
    public DeferredBatchOperation mirror(MirrorContext mirrorContext) {
        return new DeferredBatchOperation(context, () -> operations().map(o -> o.mirror(mirrorContext)));
    }

    @Override
    public DeferredBatchOperation revolve(RevolveContext revolveContext) {
        return new DeferredBatchOperation(context, () -> operations().map(o -> o.revolve(revolveContext)));
    }

    @Override
    public DeferredBatchOperation refactor(RefactorContext source) {
        return new DeferredBatchOperation(context, () -> operations().map(o -> o.refactor(source)));
    }

    @Override
    public DeferredBatchOperation map(UnaryOperator<TransformableOperation> operator) {
        return new DeferredBatchOperation(context, () -> operations().map(operator));
    }

    @Override
    public DeferredBatchOperation mapEach(UnaryOperator<TransformableOperation> operator) {
        return new DeferredBatchOperation(context, () -> operations().map(op -> {
            if (op instanceof DeferredBatchOperation op1) {
                return op1.mapEach(operator);
            } else {
                return operator.apply(op);
            }
        }));
    }

    @Override
    public DeferredBatchOperation flatten() {
        return new DeferredBatchOperation(context, () -> {
            return operations().flatMap(op -> op instanceof DeferredBatchOperation op1 ? op1.flatten().operations() : Stream.of(op));
        });
    }

    @Override
    public DeferredBatchOperation filter(Predicate<TransformableOperation> predicate) {
        return new DeferredBatchOperation(context, () -> operations().filter(predicate));
    }

}
