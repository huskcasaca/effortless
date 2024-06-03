package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ContainerBlockEntity;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
import dev.huskuraft.effortless.building.operation.Operation;

public class BlockInteractOperationResult extends BlockOperationResult {

    public BlockInteractOperationResult(
            BlockInteractOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp,
            BlockEntity blockEntityBeforeOp,
            BlockEntity blockEntityAfterOp
    ) {
        super(operation, result, blockStateBeforeOp, blockStateAfterOp, blockEntityBeforeOp, blockEntityAfterOp);
    }

    @Override
    public Operation getReverseOperation() {
        return new BlockStateUpdateOperation(
                operation.getWorld(),
                operation.getPlayer(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction(),
                getBlockStateToBreak(),
                getBlockEntityToRemove(),
                operation.getExtras()
        );
    }

    @Override
    public List<BlockState> getBlockStateSummary(BlockStateSummary blockStateSummary) {
        var blockState = switch (blockStateSummary) {
            case BLOCKS_INTERACTED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NOT_INTERACTABLE -> switch (result) {
                case FAIL_UNKNOWN -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_INTERACT_BLACKLISTED -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_INTERACT_NO_PERMISSION -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_TOOLS_INSUFFICIENT -> switch (result) {
                case FAIL_INTERACT_TOOL_INSUFFICIENT -> getBlockStateToBreak();
                default -> null;
            };
            default -> null;
        };
        if (blockState == null) {
            return List.of();
        }
        return List.of(blockState);
    }

    @Override
    public List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary) {
        switch (blockEntitySummary) {
            case CONTAINER_CONSUMED -> {
                switch (result) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> {
                        if (getBlockEntityToAdd() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
            case CONTAINER_DROPPED -> {
                switch (result) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> {
                        if (getBlockEntityToRemove() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
        }
        return List.of();
    }


}
