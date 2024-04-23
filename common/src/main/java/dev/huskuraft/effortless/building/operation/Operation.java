package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;

public abstract class Operation {

    public abstract Context getContext();

    public abstract BlockPosition locate();

    public abstract OperationResult commit();

}
