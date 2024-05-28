package dev.huskuraft.effortless.building.operation;

public abstract class OperationResult {

    public abstract Operation getOperation();

    public abstract Operation getReverseOperation();

    public abstract OperationTooltip getTooltip();

}
