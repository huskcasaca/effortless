package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;
import dev.huskuraft.effortless.core.*;

import java.util.Collections;

public final class BlockPlaceOperation extends BlockOperation {

    private final BlockData blockData;

    public BlockPlaceOperation(
            World world,
            Player player,
            Context context,
            Storage storage,
            BlockInteraction interaction,
            BlockData blockData
    ) {
        super(world, player, context, storage, interaction);
        this.blockData = blockData;
    }

    private BlockOperationResult.Type placeBlock() {

        if (blockData == null) {
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
            case DISABLED -> {
                if (!player.getWorld().getBlockData(getInteraction().getBlockPosition()).isReplaceable(player, getInteraction())) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
            }
            case NORMAL, QUICK -> {
                if (!player.getGameType().isCreative() && !player.getWorld().getBlockData(getInteraction().getBlockPosition()).isDestroyable()) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
                if (!player.canAttackBlock(getInteraction().getBlockPosition())) {
                    return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
                }
            }
        }
        // action permission
        var itemStack = storage.searchByItem(blockData.getItem()).orElse(null);

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

        // compatible layer
        var originalItemStack = player.getItemStack(InteractionHand.MAIN);
        player.setItemStack(InteractionHand.MAIN, itemStack);
        var placed = player.tryPlaceBlock(interaction, blockData, itemStack);
        player.setItemStack(InteractionHand.MAIN, originalItemStack);

        return placed ? BlockOperationResult.Type.SUCCESS : BlockOperationResult.Type.FAIL_INTERNAL;

    }

    @Override
    public BlockPlaceOperationResult commit() {
        // FIXME: 9/8/23 blockData NPE
        var type = placeBlock();
        var inputs = Collections.singletonList(blockData.getItem().getDefaultStack());
        var outputs = Collections.<ItemStack>emptyList();

        return new BlockPlaceOperationResult(this, type, inputs, outputs);
    }

    @Override
    public BlockPlaceOperation move(MoveContext moveContext) {
        return new BlockPlaceOperation(world, player, context, storage, moveContext.move(interaction), blockData);
    }

    @Override
    public BlockPlaceOperation mirror(MirrorContext mirrorContext) {
        return new BlockPlaceOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(blockData));
    }

    @Override
    public BlockPlaceOperation revolve(RevolveContext revolveContext) {
        return null;
    }

    @Override
    public BlockPlaceOperation refactor(RefactorContext refactorContext) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, refactorContext.refactor(player, getInteraction()));
    }

    @Override
    public BlockData getBlockData() {
        return blockData;
    }

}