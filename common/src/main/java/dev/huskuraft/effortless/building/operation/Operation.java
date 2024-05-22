package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.Context;

public interface Operation extends Mirrorable<Operation>, Movable<Operation>, Rotatable<Operation>, Refactorable<Operation> {

    Context getContext();

    OperationResult commit();

}
