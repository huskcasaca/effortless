package dev.huskuraft.effortless.building.operation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;

public abstract class OperationResult {

    public abstract Operation getOperation();

    public abstract Operation getReverseOperation();

    public final OperationTooltip getTooltip() {
        return OperationTooltip.build(getOperation().getContext(),
                Arrays.stream(BlockStateSummary.values()).collect(Collectors.toMap(Function.identity(), this::getBlockStateSummary)),
                Arrays.stream(BlockEntitySummary.values()).collect(Collectors.toMap(Function.identity(), this::getBlockEntitySummary)));
    }

    public abstract List<BlockState> getBlockStateSummary(BlockStateSummary blockStateSummary);

    public abstract List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary);

}
