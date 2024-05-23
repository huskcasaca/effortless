package dev.huskuraft.effortless.building.operation;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;

public abstract class OperationResult {

    public abstract Operation getOperation();

    // FIXME: 27/12/23 type
    public abstract TransformableOperation getReverseOperation();

//    public abstract boolean isSuccess();

    public abstract List<ItemStack> getSummary(OperationSummaryType type);

    public int getSuccessItemsCount() {
        var sum = 0;
        for (var value : OperationSummaryType.values()) {
            if (value.isSuccess()) {
                sum += getSummary(value).stream().mapToInt(ItemStack::getCount).sum();
            }
        }
        return sum;
    }

    public int getFailItemsCount() {
        var sum = 0;
        for (var value : OperationSummaryType.values()) {
            if (!value.isSuccess()) {
                sum += getSummary(value).stream().mapToInt(ItemStack::getCount).sum();
            }
        }
        return sum;
    }

}
