package dev.huskuraft.effortless.building.operation.block;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockPlaceOperationResult extends BlockOperationResult {

    public BlockPlaceOperationResult(
            BlockPlaceOperation operation,
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

        return new BlockBreakOperation(
                operation.getWorld(),
                operation.getPlayer(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction()
        );
    }

    @Override
    public Collection<ItemStack> getProducts(ItemType type) {
        return switch (type) {
            case PLAYER_USED -> {
                var color = getColor();
                if (color != null) {
                    yield inputs().stream().map(stack -> ItemStackUtils.putColorTag(stack, color.getRGB())).toList();
                }
                yield Collections.emptyList();
            }
            case WORLD_DROPPED -> {
                yield Collections.emptyList();
            }
        };
    }

    public Color getColor() {
        return switch (result) {
            case SUCCESS, CONSUME -> new Color(235, 235, 235);
            case FAIL_ITEM_INSUFFICIENT -> new Color(255, 0, 0);
            default -> null;
        };
    }
}
