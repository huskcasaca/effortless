package dev.huskuraft.effortless.building.operation.empty;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public final class EmptyOperation extends TransformableOperation {

    public EmptyOperation() {
    }

    @Override
    public BlockPosition locate() {
        return null;
    }

    @Override
    public TransformableOperation mirror(MirrorContext mirrorContext) {
        return this;
    }

    @Override
    public TransformableOperation move(MoveContext moveContext) {
        return this;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public OperationResult commit() {
        return new EmptyOperationResult();
    }

    @Override
    public TransformableOperation refactor(RefactorContext source) {
        return this;
    }

    @Override
    public TransformableOperation rotate(RotateContext rotateContext) {
        return this;
    }
}
