package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.core.ItemStack;

import java.util.Collection;

public abstract class OperationResult {

    public abstract Operation getOperation();

    public abstract Operation getReverseOperation();

//    public abstract boolean isSuccess();

    public abstract Collection<ItemStack> get(ItemType type);
}
