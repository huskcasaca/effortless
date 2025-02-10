package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockItem;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.DiggerItem;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.core.StatTypes;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;
import dev.huskuraft.effortless.building.session.Session;

public class BlockStateUpdateOperation extends BlockOperation {

    public BlockStateUpdateOperation(
            Session session,
            Context context,
            Storage storage,
            BlockInteraction interaction,
            BlockState blockState,
            RecordTag blockTag,
            Extras extras
    ) {
        super(session, context, storage, interaction, blockState, blockTag, extras);
    }

    protected boolean destroyBlockInternal() {
        var blockState = getBlockStateInWorld();
        var blockEntity = getWorld().getBlockEntity(getBlockPosition());
        var itemInHand = getPlayer().getItemStack(InteractionHand.MAIN);
        var itemInHandCopy = itemInHand.copy();

        blockState.getBlock().destroyStart(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);

        var removed = getWorld().removeBlock(getBlockPosition(), false);
        if (removed) {
            getWorld().getBlockState(getBlockPosition()).getBlock().destroy(getWorld(), getPlayer(), getBlockPosition(), blockState);
        }
        if (getPlayer().getGameMode().isCreative()) {
            return true;
        }
        var properTool = !blockState.requiresCorrectToolForDrops() || itemInHand.getItem().isCorrectToolForDropsNoThrows(blockState);
        itemInHand.mineBlock(getWorld(), getPlayer(), getBlockPosition(), blockState);
        if (removed && properTool) {
            blockState.getBlock().destroyEnd(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);
        }
        return true;
    }

    protected BlockOperationResultType updateBlock() {

        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return BlockOperationResultType.FAIL_WORLD_INCORRECT_DIM;
        }
        if (getPlayer().getGameMode().isSpectator()) {
            return BlockOperationResultType.FAIL_PLAYER_GAME_MODE;
        }
        if (getBlockStateInWorld() == null || getBlockState() == null) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_NULL;
        }
        if (getBlockStateInWorld().isAir() && getBlockState().isAir()) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_AIR;
        }
        if (!allowInteraction()) {
            if (!getBlockStateInWorld().isAir()) {
                return BlockOperationResultType.FAIL_BREAK_NO_PERMISSION;
            }
            if (!getBlockState().isAir()) {
                return BlockOperationResultType.FAIL_PLACE_NO_PERMISSION;
            }
        }
        if (!isInBorderBound()) {
            return BlockOperationResultType.FAIL_WORLD_BORDER;
        }
        if (!isInHeightBound()) {
            return BlockOperationResultType.FAIL_WORLD_HEIGHT;
        }

        if (!getBlockStateInWorld().isAir()) {
            if (!context.configs().constraintConfig().allowBreakBlocks()) {
                return BlockOperationResultType.FAIL_BREAK_NO_PERMISSION;
            }
            if (!context.configs().constraintConfig().whitelistedItems().isEmpty() && !context.configs().constraintConfig().whitelistedItems().contains(getBlockStateInWorld().getItem().getId()) && !getBlockStateInWorld().isAir()) {
                return BlockOperationResultType.FAIL_BREAK_BLACKLISTED;
            }
            if (!context.configs().constraintConfig().blacklistedItems().isEmpty() && context.configs().constraintConfig().blacklistedItems().contains(getBlockStateInWorld().getItem().getId()) && !getBlockStateInWorld().isAir()) {
                return BlockOperationResultType.FAIL_BREAK_BLACKLISTED;
            }
        }

        if (!getBlockState().isAir()) {
            if (!context.configs().constraintConfig().allowPlaceBlocks()) {
                return BlockOperationResultType.FAIL_PLACE_NO_PERMISSION;
            }
            if (!context.configs().constraintConfig().whitelistedItems().isEmpty() && !context.configs().constraintConfig().whitelistedItems().contains(getBlockState().getItem().getId())) {
                return BlockOperationResultType.FAIL_PLACE_BLACKLISTED;
            }
            if (!context.configs().constraintConfig().blacklistedItems().isEmpty() && context.configs().constraintConfig().blacklistedItems().contains(getBlockState().getItem().getId())) {
                return BlockOperationResultType.FAIL_PLACE_BLACKLISTED;
            }
        }


        if (!getBlockState().isAir()) { // check replace for place

            switch (context.replaceStrategy()) {
                case DISABLED -> {
                    if (!getBlockStateInWorld().canBeReplaced(getPlayer(), getInteraction())) {
                        return BlockOperationResultType.FAIL_BREAK_REPLACE_RULE;
                    }
                }
                case BLOCKS_AND_AIR -> {
                }
                case BLOCKS_ONLY -> {
                    if (!(getBlockStateInWorld().getItem() instanceof BlockItem) || getBlockStateInWorld().isAir()) {
                        return BlockOperationResultType.FAIL_BREAK_REPLACE_RULE;
                    }
                }
                case OFFHAND_ONLY -> {
                    if (context.extras().inventorySnapshot().getOffhandItems().isEmpty()) {
                        if (getBlockStateInWorld().isAir()) {
                            return BlockOperationResultType.FAIL_BREAK_REPLACE_RULE;
                        }
                    } else {
                        if (!context.extras().inventorySnapshot().getOffhandItems().stream().map(ItemStack::getItem).toList().contains(getBlockStateInWorld().getItem())) {
                            return BlockOperationResultType.FAIL_BREAK_REPLACE_RULE;
                        }
                    }
                }
            }
        }

        if (!getBlockStateInWorld().isAir()) {
            if (!getPlayer().getGameMode().isCreative() && getWorld().getBlockState(getBlockPosition()).hasTagFeatureCannotReplace()) {
                return BlockOperationResultType.FAIL_BREAK_REPLACE_FLAGS;
            }

            var durabilityReserved = getContext().getReservedToolDurability();
            var requireCorrectTool = !getPlayer().getGameMode().isCreative() && context.useProperTool() && !getBlockStateInWorld().isReplaceable();
            var miningTool = (ItemStack) null;

            if (requireCorrectTool) {
                miningTool = getStorage().contents().stream().filter(stack -> stack.getItem().isCorrectToolForDropsNoThrows(getBlockStateInWorld())).filter(tool -> !tool.isDamageableItem() || tool.getDurabilityLeft() > durabilityReserved).findFirst().orElse(null);
                if (miningTool == null) {
                    miningTool = getStorage().contents().stream().filter(tool -> tool.getItem() instanceof DiggerItem).filter(tool -> !tool.isDamageableItem() || tool.getDurabilityLeft() > durabilityReserved).findFirst().orElse(null);
                }
                if (miningTool == null) {
                    return BlockOperationResultType.FAIL_BREAK_TOOL_INSUFFICIENT;
                }
            }

            if (context.isPreviewType() || context.isBuildClientType()) {
                if (requireCorrectTool) {
                    miningTool.damage(1);
                }
            }
            if (context.isBuildType()) {
                var itemStackBeforeBreak = getPlayer().getItemStack(InteractionHand.MAIN);
                if (requireCorrectTool) {
                    getPlayer().setItemStack(InteractionHand.MAIN, miningTool);
                }
                var destroyed = destroyBlockInternal();
                if (requireCorrectTool) {
                    getPlayer().setItemStack(InteractionHand.MAIN, itemStackBeforeBreak);
                }
                if (!destroyed) {
                    return BlockOperationResultType.FAIL_UNKNOWN;
                }
            }
        }

        if (!getBlockState().isAir()) {
            var itemStack = storage.search(getBlockState().getItem()).orElse(null);

            if (itemStack == null || itemStack.isEmpty()) {
                return BlockOperationResultType.FAIL_PLACE_ITEM_INSUFFICIENT;
            }

            if (!(itemStack.getItem() instanceof BlockItem blockItem)) {
                return BlockOperationResultType.FAIL_PLACE_ITEM_NOT_BLOCK;
            }

            var estimation = getItemCountEstimation();
            if (!getPlayer().getGameMode().isCreative()) {
                if (storage.getCount(itemStack.getItem()) < estimation) {
                    return BlockOperationResultType.FAIL_PLACE_ITEM_INSUFFICIENT;
                }
            }

            if (context.isPreviewType() || context.isBuildClientType()) {
                storage.consume(itemStack.getItem(), estimation);
                return BlockOperationResultType.CONSUME;
            }

            if (context.isBuildType()) {
                var originalItemStack = getPlayer().getItemStack(getHand());
                getPlayer().setItemStack(getHand(), itemStack);
                var placed = false;
                if (context.useLegacyBlockPlace()) {
                    placed = blockItem.placeOnBlock(getPlayer(), getInteraction()).consumesAction();
                } else {
                    placed = blockItem.setBlockOnly(getWorld(), getPlayer(), getInteraction(), getBlockState());
                    if (placed /*&& getWorld().getBlockState(getBlockPosition()).getBlock() == */) {
                        // FIXME: 19/5/24
//                blockItem.updateBlockStateFromTag(getWorld(), getBlockPosition(), getNewBlockState()(), itemStack);
                        blockItem.updateBlockEntityTag(getWorld(), getBlockPosition(), getBlockState(), itemStack);
                        blockItem.getBlock().place(getWorld(), getPlayer(), getBlockPosition(), getBlockState(), itemStack);
                    }
                    if (placed && !getPlayer().getGameMode().isCreative()) {
                        storage.consume(blockItem, getItemCountEstimation());
                    }
                }
                getPlayer().setItemStack(getHand(), originalItemStack);
                if (!placed) {
                    return BlockOperationResultType.FAIL_UNKNOWN;
                }

                getPlayer().awardStat(StatTypes.ITEM_USED.get(itemStack.getItem()));

                if (context.fillContainers()) {
                    if (getEntityTag() != null && getBlockEntityInWorld() != null) {
                        if (getPlayer().getGameMode().isCreative()) {
                            getBlockEntityInWorld().setTag(getEntityTag());
                            return BlockOperationResultType.SUCCESS;
                        } else {
                            return BlockOperationResultType.SUCCESS_PARTIAL;
                        }
                    }
                    return BlockOperationResultType.SUCCESS_PARTIAL;
                }
                return BlockOperationResultType.SUCCESS;
            }
        }
        return BlockOperationResultType.CONSUME;
    }

    @Override
    public BlockStateUpdateOperationResult commit() {
        var entityExtrasBeforeOp = Extras.get(getPlayer());
        var blockStateBeforeOp = getBlockStateInWorld();
        var entityTagBeforeOp = getEntityTagInWorld();
        Extras.set(getPlayer(), getExtras());
        var result = updateBlock();
        Extras.set(getPlayer(), entityExtrasBeforeOp);
        var entityTagAfterOp = getEntityTagInWorld();
        var blockStateAfterOp = getBlockStateInWorld();

        if (getContext().isBuildClientType() && getBlockPosition().toVector3d().distance(getPlayer().getEyePosition()) <= 32) {
            if (result.success()) {
                getPlayer().getClient().getParticleEngine().destroy(getBlockPosition(), blockStateBeforeOp);
            }
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }
        return new BlockStateUpdateOperationResult(this, result, blockStateBeforeOp, blockStateAfterOp, entityTagBeforeOp, entityTagAfterOp);

    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockStateUpdateOperation(session, context, storage, moveContext.move(interaction), blockState, entityTag, extras);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateUpdateOperation(session, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(blockState), entityTag, mirrorContext.mirror(extras));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateUpdateOperation(session, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(blockState), entityTag, rotateContext.rotate(extras));
    }

    @Override
    public Operation refactor(RefactorContext refactorContext) {
        return new BlockStateUpdateOperation(session, context, storage, interaction, refactorContext.refactor(getPlayer(), getInteraction()), entityTag, extras);
    }

    @Override
    public Type getType() {
        return Type.UPDATE;
    }

    public int getItemCountEstimation() {
        return getBlockState().getRequiredItemCount();
//        return getBlockState().getBlock().getDrops(getWorld(), getPlayer(), getBlockPosition(), getBlockStateInWorld(), null, ItemStack.empty()).stream().filter(itemStack1 -> itemStack1.getItem().equals(getBlockState().getItem())).mapToInt(ItemStack::getCount).sum();
    }
}
