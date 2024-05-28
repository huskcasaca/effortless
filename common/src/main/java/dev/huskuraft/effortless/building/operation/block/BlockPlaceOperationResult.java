package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.operation.BlockSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockPlaceOperationResult extends BlockOperationResult {

    public BlockPlaceOperationResult(
            BlockPlaceOperation operation,
            Type result,
            BlockState oldBlockState,
            BlockState newBlockState
    ) {
        super(operation, result, oldBlockState, newBlockState);
    }

    @Override
    public Operation getReverseOperation() {
        if (result().fail()) {
            return new EmptyOperation();
        }

        return new BlockBreakOperation(
                operation.getWorld(),
                operation.getPlayer(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction(),
                operation.getEntityState()
        );
    }

    @Override
    public Map<BlockSummary, List<BlockState>> getBlockSummary() {
        var summary = new HashMap<BlockSummary, List<BlockState>>();
        if (getOldBlockState() != null && !getOldBlockState().isAir()) {
            summary.put(getBreakSummaryType(), List.of(getOldBlockState()));
        }
        if (getOperation().getBlockState() != null) {
            summary.compute(getSummaryType(), (k, v) -> v == null ? List.of(getOperation().getBlockState()) : List.of(getOperation().getBlockState(), v.get(0)));
        }
        return Collections.unmodifiableMap(summary);
    }

    public BlockSummary getSummaryType() {
        return switch (result) {
            case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BlockSummary.BLOCKS_PLACED;
            case FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> BlockSummary.BLOCKS_NOT_PLACEABLE;
            case FAIL_ITEM_INSUFFICIENT -> BlockSummary.BLOCKS_ITEMS_INSUFFICIENT;
            case FAIL_CONFIG_PLACE_BLACKLISTED -> BlockSummary.BLOCKS_BLACKLISTED;
            case FAIL_CONFIG_PLACE_PERMISSION -> BlockSummary.BLOCKS_NO_PERMISSION;
            default -> BlockSummary.HIDDEN;
        };
    }

    public BlockSummary getBreakSummaryType() {
        return switch (result) {
            case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BlockSummary.BLOCKS_DESTROYED;
            case FAIL_PLAYER_CANNOT_INTERACT, FAIL_PLAYER_CANNOT_BREAK -> BlockSummary.BLOCKS_NOT_BREAKABLE;
            case FAIL_TOOL_INSUFFICIENT -> BlockSummary.BLOCKS_TOOLS_INSUFFICIENT;
            case FAIL_CONFIG_BREAK_BLACKLISTED -> BlockSummary.BLOCKS_BLACKLISTED;
            case FAIL_CONFIG_BREAK_PERMISSION -> BlockSummary.BLOCKS_NO_PERMISSION;
            default -> BlockSummary.HIDDEN;
        };
    }

}
