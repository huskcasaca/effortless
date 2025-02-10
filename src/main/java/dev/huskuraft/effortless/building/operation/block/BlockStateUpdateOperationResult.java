package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.ContainerBlockEntity;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockStateUpdateOperationResult extends BlockOperationResult {

    public BlockStateUpdateOperationResult(
            BlockStateUpdateOperation operation,
            BlockOperationResultType result,
            BlockState blockStateBeforeOp,
            BlockState blockStateAfterOp,
            RecordTag entityTagBeforeOp,
            RecordTag entityTagAfterOp
    ) {
        super(operation, result, blockStateBeforeOp, blockStateAfterOp, entityTagBeforeOp, entityTagAfterOp);
    }

    @Override
    public Operation getReverseOperation() {
        if (result().fail()) {
            return new EmptyOperation(operation.getContext());
        }

        return new BlockStateUpdateOperation(
                operation.getSession(),
                operation.getContext(),
                operation.getStorage(),
                operation.getInteraction(),
                getBlockStateToBreak(),
                getEntityTagToBreak(),
                operation.getExtras()
        );
    }

    @Override
    public List<ItemStack> getItemSummary(ItemSummary itemSummary) {
        switch (itemSummary) {
            case CONTAINER_CONSUMED -> {
                switch (result) {
                    case SUCCESS, CONSUME -> {
                        if (getBlockEntityToPlace() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
            case CONTAINER_DROPPED -> {
                switch (result) {
                    case SUCCESS, CONSUME -> {
                        if (getBlockEntityToBreak() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
        }
        var blockState = switch (itemSummary) {
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
            case BLOCKS_NOT_REPLACEABLE -> switch (result) {
                case FAIL_BREAK_REPLACE_RULE, FAIL_BREAK_REPLACE_FLAGS -> getBlockStateToBreak();
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
            case CONTAINER_CONSUMED -> null;
            case CONTAINER_DROPPED -> null;
        };
        if (blockState == null) {
            return List.of();
        }
        return List.of(blockState.getItem().getDefaultStack().withCount(blockState.getRequiredItemCount()));
//        return blockState.getBlock().getDrops(getOperation().getWorld(), getOperation().getPlayer(), getOperation().getBlockPosition(), blockState, null, ItemStack.empty());
    }



}
