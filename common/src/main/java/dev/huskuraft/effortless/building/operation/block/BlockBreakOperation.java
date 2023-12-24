package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;
import dev.huskuraft.effortless.core.*;

import java.util.Collections;

public final class BlockBreakOperation extends BlockOperation {

    public BlockBreakOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction
    ) {
        super(world, player, context, storage, interaction);
    }

    private BlockOperationResult.Type breakBlock() {

        // spectator
        if (player.getGameType().isSpectator()) { // move
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_MODIFY;
        }

        // action permission
        if (!player.canInteractBlock(getInteraction())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_INTERACT;
        }

//        // action permission
//        if (!player.canAttackByTool(blockPos)) {
//            return BlockInteractionResult.FAIL_PLAYER_CANNOT_ATTACK;
//        }

        // world permission
        if (!player.getGameType().isCreative() && !player.getWorld().getBlockData(getInteraction().getBlockPosition()).isDestroyable()) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }
        // player permission
        if (!player.canAttackBlock(getInteraction().getBlockPosition())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }

        if (world.getBlockData(getInteraction().getBlockPosition()).isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }
        if (context.isPreview() && world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }

        if (player.breakBlock(getInteraction())) {
            return BlockOperationResult.Type.SUCCESS;
        } else {
            return BlockOperationResult.Type.FAIL_INTERNAL;
        }
    }

    @Override
    public BlockBreakOperationResult commit() {
        var type = breakBlock();
        var inputs = Collections.<ItemStack>emptyList();
        var outputs = Collections.singletonList(world.getBlockData(interaction.getBlockPosition()).getItem().getDefaultStack());

        return new BlockBreakOperationResult(this, type, inputs, outputs);
    }

    @Override
    public BlockBreakOperation move(MoveContext moveContext) {
        return new BlockBreakOperation(world, player, context, storage, moveContext.move(interaction));
    }

    @Override
    public BlockBreakOperation mirror(MirrorContext mirrorContext) {
        return new BlockBreakOperation(world, player, context, storage, mirrorContext.mirror(interaction));
    }

    @Override
    public BlockBreakOperation revolve(RevolveContext revolveContext) {
        return new BlockBreakOperation(world, player, context, storage, revolveContext.revolve(interaction));
    }

    @Override
    public BlockBreakOperation refactor(RefactorContext source) {
        return this;
    }

    @Override
    public BlockData getBlockData() {
        return world.getBlockData(interaction.getBlockPosition());
    }
}