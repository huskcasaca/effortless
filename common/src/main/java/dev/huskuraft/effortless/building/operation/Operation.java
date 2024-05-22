package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;

public abstract class Operation implements Mirrorable<Operation>, Movable<Operation>, Rotatable<Operation>, Refactorable<Operation>  {

    public abstract Context getContext();

    public abstract BlockPosition locate();

    public abstract OperationResult commit();

}
