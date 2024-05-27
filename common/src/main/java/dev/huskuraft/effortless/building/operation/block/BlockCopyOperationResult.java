package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationSummaryType;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockCopyOperationResult extends BlockOperationResult {

    public BlockCopyOperationResult(
            BlockCopyOperation operation,
            Type result,
            List<ItemStack> inputs,
            List<ItemStack> outputs
    ) {
        super(operation, result, inputs, outputs);
    }

    @Override
    public Operation getReverseOperation() {
        return new EmptyOperation();
    }

    @Override
    public List<ItemStack> getSummary(OperationSummaryType type) {
        return switch (type) {
            case BLOCKS_INTERACTED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> outputs;
                default -> List.of();
            };
            case BLOCKS_NOT_INTERACTABLE -> switch (result) {
                case FAIL_PLAYER_CANNOT_INTERACT, FAIL_PLAYER_CANNOT_BREAK, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> outputs;
                default -> List.of();
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_CONFIG_BLACKLISTED -> outputs;
                default -> List.of();
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_CONFIG_BREAK_PERMISSION -> outputs;
                default -> List.of();
            };
            default -> List.of();
        };
    }

    public BlockPosition getRelativePosition() {
        return getOperation().getInteraction().blockPosition().sub(getOperation().getContext().getInteractions().get(0).blockPosition());
    }

    public BlockSnapShot getBlockSnapshot() {
        return new BlockSnapShot(getRelativePosition(), getOperation().getBlockState());
    }

    public record BlockSnapShot(
            BlockPosition relativePosition,
            BlockState blockState
    ) {

    }

}
