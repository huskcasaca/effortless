package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.core.ItemStack;

import java.util.Collection;

public abstract class OperationResult {

    public abstract Operation getOperation();

    // FIXME: 27/12/23 type
    public abstract TransformableOperation getReverseOperation();

//    public abstract boolean isSuccess();

    public abstract Collection<ItemStack> getProducts(ItemType type);

}
