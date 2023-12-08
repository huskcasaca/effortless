package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.core.ItemStack;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public BlockBreakOperation getReverseOperation() {
        return null;
    }

    @Override
    public Collection<ItemStack> get(ItemType type) {
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
            case FAIL_ITEM_INSUFFICIENT -> new Color(235, 235, 235);
            default -> null;
        };
    }
}