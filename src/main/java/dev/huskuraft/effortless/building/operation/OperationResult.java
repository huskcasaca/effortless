package dev.huskuraft.effortless.building.operation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.huskuraft.universal.api.core.ItemStack;

public abstract class OperationResult {

    public abstract Operation getOperation();

    public abstract Operation getReverseOperation();

    public final OperationTooltip getTooltip() {
        return OperationTooltip.build(getOperation().getContext(),
                Arrays.stream(ItemSummary.values()).collect(Collectors.toMap(Function.identity(), this::getItemSummary))
        );
    }

    public abstract int getAffectedBlockCount();

    public abstract List<ItemStack> getItemSummary(ItemSummary itemSummary);

}
