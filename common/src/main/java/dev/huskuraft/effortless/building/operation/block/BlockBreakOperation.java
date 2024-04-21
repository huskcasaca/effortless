package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.SoundInstance;
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

    private BlockOperationResult.Type breakBlock() {

        // spectator
        if (player.getGameType().isSpectator()) { // move
            return BlockOperationResult.Type.FAIL_PLAYER_IS_SPECTATOR;
        }

        // whitelist/blacklist
        if (!context.limitationParams().generalConfig().whitelistedItems().isEmpty() && !context.limitationParams().generalConfig().whitelistedItems().contains(getItemStack().getItem().getId())) {
            return BlockOperationResult.Type.FAIL_WHITELISTED;
        }

        if (!context.limitationParams().generalConfig().blacklistedItems().isEmpty() && context.limitationParams().generalConfig().blacklistedItems().contains(getItemStack().getItem().getId())) {
            return BlockOperationResult.Type.FAIL_BLACKLISTED;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_HEIGHT;
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

//        if (context.type() == BuildType.COMMAND) {
//            CommandManager.dispatch(new SetBlockCommand(Items.AIR.item().getDefaultStack().getBlockState(getPlayer(), getInteraction()), getInteraction().getBlockPosition(), SetBlockCommand.Mode.REPLACE));
//            return BlockOperationResult.Type.SUCCESS;
//        }


        if (player.tryBreakBlock(getInteraction())) {
            return BlockOperationResult.Type.SUCCESS;
        } else {
            return BlockOperationResult.Type.FAIL_UNKNOWN;
        }
    }

    @Override
    public BlockBreakOperationResult commit() {
        var inputs = Collections.<ItemStack>emptyList();
        var outputs = Collections.singletonList(getItemStack());
        var result = breakBlock();

        if (getWorld().isClient() && getContext().isPreviewOnce()) {
            if (result.success()) {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().breakSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 2.0F, getBlockState().getSoundSet().pitch() * 0.8F, getInteraction().getBlockPosition().getCenter());
                getPlayer().getClient().getSoundManager().play(sound);
            } else {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().hitSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 8.0F, getBlockState().getSoundSet().pitch() * 0.5F, getInteraction().getBlockPosition().getCenter());
                getPlayer().getClient().getSoundManager().play(sound);
            }
        }

        return new BlockBreakOperationResult(this, result, inputs, outputs);
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

    private ItemStack getItemStack() {
        return world.getBlockState(interaction.getBlockPosition()).getItem().getDefaultStack();
    }
}
