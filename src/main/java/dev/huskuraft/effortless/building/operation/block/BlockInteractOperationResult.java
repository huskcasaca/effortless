package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.ContainerBlockEntity;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.Operation;

public class BlockInteractOperationResult extends BlockOperationResult {

    public BlockInteractOperationResult(
            BlockInteractOperation operation,
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
                        if (getEntityTagToPlace() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
            case CONTAINER_DROPPED -> {
                switch (result) {
                    case SUCCESS, CONSUME -> {
                        if (getEntityTagToBreak() instanceof ContainerBlockEntity containerBlockEntity) {
                            return containerBlockEntity.getItems();
                        }
                    }
                }
            }
        }
        var blockState = switch (itemSummary) {
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
        return List.of(blockState.getItem().getDefaultStack());
    }


}
