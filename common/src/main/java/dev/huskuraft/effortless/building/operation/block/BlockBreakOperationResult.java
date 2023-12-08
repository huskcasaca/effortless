package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.core.ItemStack;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlockBreakOperationResult extends BlockOperationResult {

    public BlockBreakOperationResult(
            BlockBreakOperation operation,
            Type result,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        super(operation, result, inputs, outputs);
    }

    @Override
    public BlockPlaceOperation getReverseOperation() {
        return null;
    }

    @Override
    public Collection<ItemStack> get(ItemType type) {
        return switch (type) {
            case PLAYER_USED -> {
                yield Collections.emptyList();
            }
            case WORLD_DROPPED -> {
                if (result.success()) {
                    var color = getColor();
                    if (color != null) {
                        yield outputs().stream().map(stack -> ItemStackUtils.putColorTag(stack, color.getRGB())).toList();
                    }
                }
                yield Collections.emptyList();
            }
        };
    }

    public Color getColor() {
        return switch (result) {
            case SUCCESS, CONSUME -> new Color(235, 235, 235);
            default -> null;
        };
    }

}