package dev.huskuraft.effortless.building.operation;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;

public abstract class OperationResult {

    public abstract Operation getOperation();

    // FIXME: 27/12/23 type
    public abstract TransformableOperation getReverseOperation();

//    public abstract boolean isSuccess();

    public abstract List<ItemStack> getProducts(ItemSummaryType type);

}
