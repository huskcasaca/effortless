package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ContainerBlockEntity;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockStateUpdateOperationResult extends BlockOperationResult {

    public BlockStateUpdateOperationResult(
            BlockStateUpdateOperation operation,
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
        if (result().fail()) {
            return new EmptyOperation(operation.getContext());
        }

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
            case BLOCKS_PLACED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> getBlockStateToPlace();
                default -> null;
            };
            case BLOCKS_DESTROYED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_INTERACTED -> null;
            case BLOCKS_COPIED -> null;
            case BLOCKS_NOT_PLACEABLE -> switch (result) {
                case FAIL_BREAK_REPLACE_RULE, FAIL_BREAK_REPLACE_FLAGS -> getBlockStateToPlace();
                default -> null;
            };
            case BLOCKS_NOT_BREAKABLE -> switch (result) {
                case FAIL_BREAK_REPLACE_FLAGS -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NOT_INTERACTABLE -> null;
            case BLOCKS_NOT_COPYABLE -> null;
            case BLOCKS_ITEMS_INSUFFICIENT -> switch (result) {
                case FAIL_PLACE_ITEM_INSUFFICIENT -> getBlockStateToPlace();
                default -> null;
            };
            case BLOCKS_TOOLS_INSUFFICIENT -> switch (result) {
                case FAIL_BREAK_TOOL_INSUFFICIENT -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_BREAK_BLACKLISTED -> getBlockStateToBreak();
                case FAIL_PLACE_BLACKLISTED -> getBlockStateToPlace();
                default -> null;
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_BREAK_NO_PERMISSION -> getBlockStateToBreak();
                case FAIL_PLACE_NO_PERMISSION -> getBlockStateToPlace();
                default -> null;
            };
        };
        if (blockState == null) {
            return List.of();
        }
        return List.of(blockState);
    }

    @Override
    public List<ItemStack> getBlockEntitySummary(BlockEntitySummary blockEntitySummary) {
        switch (blockEntitySummary) {
            case CONTAINER_ADDED -> {
                switch (result) {
                    case SUCCESS, SUCCESS_PARTIAL, CONSUME -> {
                        if (getBlockEntityToAdd() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
            case CONTAINER_REMOVED -> {
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
