package dev.huskuraft.effortless.building.operation.empty;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
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
    public List<BlockState> getBlockStateSummary(BlockStateSummary blockStateSummary) {
        return List.of();
    }

    @Override
    public List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary) {
        return List.of();
    }

}
