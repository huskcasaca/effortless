package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;
import dev.huskuraft.effortless.building.replace.ReplaceMode;

import java.util.Collections;

public final class BlockPlaceOperation extends BlockOperation {

    public BlockPlaceOperation(
            World world,
            Player player,
            Context context,
            Storage storage,
            BlockInteraction interaction,
            BlockState blockState
    ) {
        super(world, player, context, storage, interaction, blockState);
    }

    private BlockOperationResult.Type placeBlock() {

        if (blockState == null) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_NULL;
        }

        // spectator
        if (player.getGameType().isSpectator()) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_MODIFY;
        }

        // action permission
        if (!player.canInteractBlock(getInteraction().getBlockPosition())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_INTERACT;
        }

        switch (context.replaceMode()) {
            case DISABLED, NORMAL -> {
                if (!player.getWorld().getBlockState(getInteraction().getBlockPosition()).isReplaceable(player, getInteraction())) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
            }
            case QUICK -> {
                if (!player.getGameType().isCreative() && !player.getWorld().getBlockState(getInteraction().getBlockPosition()).isDestroyable()) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
                if (!player.canAttackBlock(getInteraction().getBlockPosition())) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
            }
        }
        // action permission
        var itemStack = storage.searchByItem(blockState.getItem()).orElse(null);

        if (itemStack == null || itemStack.isEmpty()) {
            return BlockOperationResult.Type.FAIL_ITEM_INSUFFICIENT;
        }

        if (!itemStack.getItem().isBlockItem()) {
            return BlockOperationResult.Type.FAIL_ITEM_NOT_BLOCK;
        }

        if (context.isPreview() && player.getWorld().isClient()) {
            itemStack.decrease(1);
            return BlockOperationResult.Type.CONSUME;
        }

        if (context.replaceMode() == ReplaceMode.QUICK && !player.tryBreakBlock(getInteraction())) {
            return BlockOperationResult.Type.FAIL_INTERNAL;
        }

        // compatible layer
        var originalItemStack = player.getItemStack(InteractionHand.MAIN);
        player.setItemStack(InteractionHand.MAIN, itemStack);
        var placed = player.tryPlaceBlock(interaction);
        player.setItemStack(InteractionHand.MAIN, originalItemStack);

        if (!placed) {
            return BlockOperationResult.Type.FAIL_INTERNAL;
        }

        if (!world.getBlockState(getInteraction().getBlockPosition()).equals(blockState) && !world.setBlockState(getInteraction().getBlockPosition(), blockState)) {
            return BlockOperationResult.Type.FAIL_INTERNAL;
        }

        return BlockOperationResult.Type.SUCCESS;
    }

    @Override
    public BlockPlaceOperationResult commit() {
        // FIXME: 9/8/23 blockState NPE
        var type = placeBlock();
        var inputs = Collections.singletonList(blockState.getItem().getDefaultStack());
        var outputs = Collections.<ItemStack>emptyList();

        return new BlockPlaceOperationResult(this, type, inputs, outputs);
    }

    @Override
    public BlockPlaceOperation move(MoveContext moveContext) {
        return new BlockPlaceOperation(world, player, context, storage, moveContext.move(interaction), blockState);
    }

    @Override
    public BlockPlaceOperation mirror(MirrorContext mirrorContext) {
        return new BlockPlaceOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(blockState));
    }

    @Override
    public BlockPlaceOperation revolve(RevolveContext revolveContext) {
        return null;
    }

    @Override
    public BlockPlaceOperation refactor(RefactorContext refactorContext) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, refactorContext.refactor(player, getInteraction()));
    }

}