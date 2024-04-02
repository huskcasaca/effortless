package dev.huskuraft.effortless.building.operation;

import java.awt.*;
import java.util.Collection;

import dev.huskuraft.effortless.api.core.ItemStack;

public abstract class OperationResult {

    public abstract Operation getOperation();

    // FIXME: 27/12/23 type
    public abstract TransformableOperation getReverseOperation();

//    public abstract boolean isSuccess();

    public abstract Collection<ItemStack> getProducts(ItemType type);

    public abstract Color getColor();

}
