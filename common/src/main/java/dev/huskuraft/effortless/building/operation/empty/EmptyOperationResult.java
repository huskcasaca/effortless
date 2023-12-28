package dev.huskuraft.effortless.building.operation.empty;

import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.core.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class EmptyOperationResult extends OperationResult {

    @Override
    public Operation getOperation() {
        return new EmptyOperation();
    }

    @Override
    public EmptyOperation getReverseOperation() {
        return new EmptyOperation();
    }

    @Override
    public Collection<ItemStack> getProducts(ItemType type) {
        return Collections.emptyList();
    }

}