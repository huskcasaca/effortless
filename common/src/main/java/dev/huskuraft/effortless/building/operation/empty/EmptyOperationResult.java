package dev.huskuraft.effortless.building.operation.empty;

import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationResult;

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
    public List<ItemStack> getProducts(ItemSummaryType type) {
        return Collections.emptyList();
    }

}
