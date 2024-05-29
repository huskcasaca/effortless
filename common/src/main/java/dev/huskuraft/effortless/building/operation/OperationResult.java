package dev.huskuraft.effortless.building.operation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockState;

public abstract class OperationResult {

    public abstract Operation getOperation();

    public abstract Operation getReverseOperation();

    public final OperationTooltip getTooltip() {
        return OperationTooltip.build(getOperation().getContext(), Arrays.stream(BlockSummary.values()).collect(Collectors.toMap(Function.identity(), this::getBlockSummary)), Map.of());
    }

    public abstract List<BlockState> getBlockSummary(BlockSummary blockSummary);

//    public abstract List<BlockState> getEntitySummary(EntitySummary entitySummary);

}
