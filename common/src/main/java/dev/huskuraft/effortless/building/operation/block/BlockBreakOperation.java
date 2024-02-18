package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;

import dev.huskuraft.effortless.api.command.CommandManager;
import dev.huskuraft.effortless.api.command.SetBlockCommand;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;

public class BlockBreakOperation extends BlockOperation {

    public BlockBreakOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()));
    }

    private BlockOperationResult.Type tryBreakBlock() {

        // spectator
        if (player.getGameType().isSpectator()) { // move
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_MODIFY;
        }

        // action permission
        if (!player.canInteractBlock(getInteraction().getBlockPosition())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_INTERACT;
        }

//        // action permission
//        if (!player.canAttackByTool(blockPos)) {
//            return BlockInteractionResult.FAIL_PLAYER_CANNOT_ATTACK;
//        }

        // world permission
        if (!player.getGameType().isCreative() && !player.getWorld().getBlockState(getInteraction().getBlockPosition()).isDestroyable()) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }
        // player permission
        if (!player.canAttackBlock(getInteraction().getBlockPosition())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }

        if (world.getBlockState(getInteraction().getBlockPosition()).isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }
        if (context.isPreview() && world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }

        if (context.type() == BuildType.COMMAND) {
            CommandManager.dispatch(new SetBlockCommand(Items.AIR.item().getDefaultStack().getBlockState(getPlayer(), getInteraction()), getInteraction().getBlockPosition(), SetBlockCommand.Mode.REPLACE));
            return BlockOperationResult.Type.SUCCESS;
        }

        if (player.tryBreakBlock(getInteraction())) {
            return BlockOperationResult.Type.SUCCESS;
        } else {
            return BlockOperationResult.Type.FAIL_INTERNAL;
        }
    }

    @Override
    public BlockBreakOperationResult commit() {
        var type = tryBreakBlock();
        var inputs = Collections.<ItemStack>emptyList();
        var outputs = Collections.singletonList(world.getBlockState(interaction.getBlockPosition()).getItem().getDefaultStack());

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

}
