package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockPlaceOnClientOperationResult extends BlockPlaceOperationResult {

    public BlockPlaceOnClientOperationResult(
            BlockPlaceOnClientOperation operation,
            Type result,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        super(operation, result, inputs, outputs);
    }

    @Override
    public TransformableOperation getReverseOperation() {
        if (result().fail()) {
            return new EmptyOperation();
        }

        return new BlockBreakOnClientOperation(
                operation.getWorld(),
                operation.getPlayer(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction()
        );
    }

}
