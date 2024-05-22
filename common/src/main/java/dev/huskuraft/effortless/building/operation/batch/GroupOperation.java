package dev.huskuraft.effortless.building.operation.batch;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public class GroupOperation extends BatchOperation {

    protected final List<Operation> operations;

    public GroupOperation(Context context, List<Operation> operations) {
        super(context);
        this.operations = operations;
    }

    public GroupOperation(Context context, Stream<? extends Operation> operations) {
        super(context);
        this.operations = operations.collect(Collectors.toList());
    }

    @Override
    public BatchOperationResult commit() {
        return new BatchOperationResult(this, operations().map(Operation::commit).toList());
    }

    @Override
    public GroupOperation move(MoveContext moveContext) {
        return new GroupOperation(context, operations().map(o -> o.move(moveContext)));
    }

    @Override
    public GroupOperation mirror(MirrorContext mirrorContext) {
        return new GroupOperation(context, operations().map(o -> o.mirror(mirrorContext)));
    }

    @Override
    public GroupOperation rotate(RotateContext rotateContext) {
        return new GroupOperation(context, operations().map(o -> o.rotate(rotateContext)));
    }

    @Override
    public GroupOperation refactor(RefactorContext source) {
        return new GroupOperation(context, operations().map(o -> o.refactor(source)));
    }

    @Override
    public GroupOperation map(UnaryOperator<Operation> operator) {
        return new GroupOperation(context, operations().map(operator));
    }

    @Override
    public GroupOperation mapEach(UnaryOperator<Operation> operator) {
        return new GroupOperation(context, operations().map(op -> {
            if (op instanceof BatchOperation op1) {
                return op1.mapEach(operator);
            } else {
                return operator.apply(op);
            }
        }));
    }

    @Override
    public GroupOperation flatten() {
        return new GroupOperation(context, operations().flatMap(op -> op instanceof BatchOperation op1 ? op1.flatten().operations() : Stream.of(op)));
    }

    @Override
    public GroupOperation filter(Predicate<Operation> predicate) {
        return new GroupOperation(context, operations().filter(predicate));
    }

    @Override
    public Stream<? extends Operation> operations() {
        return operations.stream();
    }

}
