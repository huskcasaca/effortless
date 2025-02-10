package dev.huskuraft.effortless.building.operation.empty;

import java.util.List;

import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationResult;

public class EmptyOperationResult extends OperationResult {

    private final EmptyOperation operation;

    public EmptyOperationResult(EmptyOperation operation) {

        this.operation = operation;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public EmptyOperation getReverseOperation() {
        return operation;
    }

    @Override
    public int getAffectedBlockCount() {
        return 0;
    }

    @Override
    public List<ItemStack> getItemSummary(ItemSummary itemSummary) {
        return List.of();
    }

}
