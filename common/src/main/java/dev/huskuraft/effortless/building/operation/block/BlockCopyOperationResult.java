package dev.huskuraft.effortless.building.operation.block;

import java.util.List;
import java.util.Map;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.building.clipboard.BlockSnapshot;
import dev.huskuraft.effortless.building.operation.BlockSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockCopyOperationResult extends BlockOperationResult {

    public BlockCopyOperationResult(
            BlockCopyOperation operation,
            Type result,
            BlockState oldBlockState,
            BlockState newBlockState
    ) {
        super(operation, result, oldBlockState, newBlockState);
    }

    @Override
    public Operation getReverseOperation() {
        return new EmptyOperation();
    }

    public BlockPosition getRelativePosition() {
        return getOperation().getInteraction().blockPosition().sub(getOperation().getContext().getInteractions().get(0).blockPosition());
    }

    public BlockSnapshot getBlockSnapshot() {
        return new BlockSnapshot(getRelativePosition(), getOperation().getBlockState());
    }

    @Override
    public Map<BlockSummary, List<BlockState>> getBlockSummary() {
        if (getOldBlockState() != null) {
            return Map.of(getSummaryType(), List.of(getOldBlockState()));
        }
        return Map.of();
    }

    public BlockSummary getSummaryType() {
        return switch (result) {
            case SUCCESS, SUCCESS_PARTIAL, CONSUME -> BlockSummary.BLOCKS_COPIED;
            case FAIL_BREAK_REPLACE_RULE, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT ->
                    BlockSummary.BLOCKS_NOT_COPYABLE;
            case FAIL_PLACE_ITEM_INSUFFICIENT -> BlockSummary.BLOCKS_ITEMS_INSUFFICIENT;
            case FAIL_BREAK_TOOL_INSUFFICIENT -> BlockSummary.BLOCKS_TOOLS_INSUFFICIENT;
            case FAIL_CONFIG_BREAK_BLACKLISTED -> BlockSummary.BLOCKS_BLACKLISTED;
            case FAIL_PLACE_NO_PERMISSION, FAIL_BREAK_NO_PERMISSION, FAIL_INTERACT_NO_PERMISSION ->
                    BlockSummary.BLOCKS_NO_PERMISSION;
            default -> BlockSummary.HIDDEN;
        };
    }

}
