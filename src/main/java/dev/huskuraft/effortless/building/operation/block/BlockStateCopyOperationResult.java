package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.ContainerBlockEntity;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.clipboard.BlockData;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;

public class BlockStateCopyOperationResult extends BlockOperationResult {

    public BlockStateCopyOperationResult(
            BlockStateCopyOperation operation,
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
        return new EmptyOperation(operation.getContext());
    }

    public BlockPosition getRelativePosition() {
        return getOperation().getInteraction().blockPosition().sub(getOperation().getContext().getInteractions().get(0).blockPosition());
    }

    public BlockData getBlockData() {
        if (getOperation().getBlockState().isAir()) {
            return new BlockData(getRelativePosition(), null, null);
        }
        return new BlockData(getRelativePosition(), getOperation().getBlockState(), getOperation().getEntityTag());
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
            case BLOCKS_COPIED -> switch (result) {
                case SUCCESS, SUCCESS_PARTIAL, CONSUME -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NOT_COPYABLE -> switch (result) {
                case FAIL_BREAK_REPLACE_RULE, FAIL_WORLD_BORDER, FAIL_WORLD_HEIGHT -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_BLACKLISTED -> switch (result) {
                case FAIL_COPY_BLACKLISTED -> getBlockStateToBreak();
                default -> null;
            };
            case BLOCKS_NO_PERMISSION -> switch (result) {
                case FAIL_COPY_NO_PERMISSION -> getBlockStateToBreak();
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
