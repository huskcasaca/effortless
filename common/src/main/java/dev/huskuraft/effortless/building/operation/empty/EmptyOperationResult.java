package dev.huskuraft.effortless.building.operation.empty;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.operation.BlockSummary;
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
    public List<BlockState> getBlockSummary(BlockSummary blockSummary) {
        return List.of();
    }

}
