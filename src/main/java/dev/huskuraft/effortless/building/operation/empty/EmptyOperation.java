package dev.huskuraft.effortless.building.operation.empty;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public final class EmptyOperation implements Operation {

    private final Context context;

    public EmptyOperation(Context context) {
        this.context = context;
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        return this;
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return this;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public OperationResult commit() {
        return new EmptyOperationResult(this);
    }

    @Override
    public Operation refactor(RefactorContext source) {
        return this;
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        return this;
    }
}
