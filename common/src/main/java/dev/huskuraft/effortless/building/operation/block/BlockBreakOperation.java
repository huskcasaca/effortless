package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

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
        if (player.getGameMode().isBlockPlacingRestricted()) { // move
            return BlockOperationResult.Type.FAIL_PLAYER_IS_SPECTATOR;
        }

        // whitelist/blacklist
        if (!context.customParams().generalConfig().whitelistedItems().isEmpty() && !context.customParams().generalConfig().whitelistedItems().contains(getItemStack().getItem().getId())) {
            return BlockOperationResult.Type.FAIL_WHITELISTED;
        }

        if (!context.customParams().generalConfig().blacklistedItems().isEmpty() && context.customParams().generalConfig().blacklistedItems().contains(getItemStack().getItem().getId())) {
            return BlockOperationResult.Type.FAIL_BLACKLISTED;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_HEIGHT;
        }

//        // action permission
//        if (!player.canAttackByTool(blockPos)) {
//            return BlockInteractionResult.FAIL_PLAYER_CANNOT_ATTACK;
//        }

        // world permission
        if (!player.getGameMode().isCreative() && !player.getWorld().getBlockState(getBlockPosition()).isDestroyable()) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }

        if (world.getBlockState(getBlockPosition()).isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }

        var blockState = world.getBlockState(getBlockPosition());
        var reservedDamage = 1;
        var useCorrectTool = !player.getGameMode().isCreative() && context.useCorrectTool();
        var correctTool = getStorage().contents().stream().filter(itemStack -> itemStack.getItem().isCorrectToolForDrops(blockState)).filter(itemStack -> !itemStack.isDamageableItem() || itemStack.getRemainingDamage() > reservedDamage).findFirst();

        if (useCorrectTool && correctTool.isEmpty()) {
            return BlockOperationResult.Type.FAIL_TOOL_INSUFFICIENT;
        }

        if (context.isPreview() && world.isClient()) {
            if (useCorrectTool) {
                correctTool.get().damageBy(player, 1);
            }
            return BlockOperationResult.Type.CONSUME;
        }

//        if (context.buildType() == BuildType.COMMAND) {
//            CommandManager.dispatch(new SetBlockCommand(Items.AIR.item().getDefaultStack().getBlockState(getPlayer(), getInteraction()), getBlockPosition(), SetBlockCommand.Mode.REPLACE));
//            return BlockOperationResult.Type.SUCCESS;
//        }

        if (world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }

        var oldItem = player.getItemStack(InteractionHand.MAIN);
        if (useCorrectTool) {
            player.setItemStack(InteractionHand.MAIN, correctTool.get());
        }

        var destroyed  = player.destroyBlock(getInteraction());

        if (useCorrectTool) {
            player.setItemStack(InteractionHand.MAIN, oldItem);
        }
        if (destroyed) {
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

        if (getWorld().isClient() && getContext().isPreviewSound()) {
            if (result.success()) {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().breakSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 2.0F * 0.5F, getBlockState().getSoundSet().pitch() * 0.8F, getBlockPosition().getCenter());
                getPlayer().getClient().getSoundManager().play(sound);
            } else {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().hitSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 8.0F * 0.5F, getBlockState().getSoundSet().pitch() * 0.5F, getBlockPosition().getCenter());
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
    public BlockBreakOperation rotate(RotateContext rotateContext) {
        return new BlockBreakOperation(world, player, context, storage, rotateContext.rotate(interaction));
    }

    @Override
    public BlockBreakOperation refactor(RefactorContext source) {
        return this;
    }

    private ItemStack getItemStack() {
        return world.getBlockState(interaction.getBlockPosition()).getItem().getDefaultStack();
    }
}
