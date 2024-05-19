package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
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
    public List<ItemStack> getProducts(ItemSummaryType type) {
        return switch (type) {
            case BLOCKS_PLACED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> inputs;
                default -> List.of();
            };
            case BLOCKS_PLACE_INSUFFICIENT -> switch (result) {
                case FAIL_ITEM_INSUFFICIENT -> inputs;
                default -> List.of();
            };
            case BLOCKS_NOT_PLACEABLE -> switch (result) {
                case FAIL_PLAYER_CANNOT_INTERACT, FAIL_PLAYER_CANNOT_BREAK, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> inputs;
                case FAIL_CONFIG_BREAK_PERMISSION, FAIL_CONFIG_PLACE_PERMISSION -> inputs;
                default -> List.of();
            };
            case BLOCKS_PLACE_NOT_WHITELISTED -> switch (result) {
                case FAIL_CONFIG_WHITELISTED -> inputs;
                default -> List.of();
            };
            case BLOCKS_PLACE_BLACKLISTED -> switch (result) {
                case FAIL_CONFIG_BLACKLISTED -> inputs;
                default -> List.of();
            };
            default -> List.of();
        };
    }

}
