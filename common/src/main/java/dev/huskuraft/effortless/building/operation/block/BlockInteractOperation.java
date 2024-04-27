package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;

public class BlockInteractOperation extends BlockOperation {

    public BlockInteractOperation(
            World world,
            Player player,
            Context context,
            Storage storage,
            BlockInteraction interaction
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()));
    }

    private BlockOperationResult.Type interactBlock() {

        if (blockState == null) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_NULL;
        }

        // spectator
        if (player.getGameType().isSpectator()) {
            return BlockOperationResult.Type.FAIL_PLAYER_IS_SPECTATOR;
        }

        // whitelist/blacklist
        if (!context.customParams().generalConfig().whitelistedItems().isEmpty() && !context.customParams().generalConfig().whitelistedItems().contains(blockState.getItem().getId())) {
            return BlockOperationResult.Type.FAIL_WHITELISTED;
        }

        if (!context.customParams().generalConfig().blacklistedItems().isEmpty() && context.customParams().generalConfig().blacklistedItems().contains(blockState.getItem().getId())) {
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
        var itemStack = storage.search(player.getItemStack(InteractionHand.MAIN).getItem()).orElse(Items.AIR.getDefaultStack());

//        if (itemStack == null) {
//            return BlockOperationResult.Type.FAIL_ITEM_INSUFFICIENT;
//        }
//
//        if (blockState.isAir()) {
//            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
//        }
//
//        if (!itemStack.getItem().isBlockItem()) {
//            return BlockOperationResult.Type.FAIL_ITEM_NOT_BLOCK;
//        }

        // action permission
        if (!player.canInteractBlock(getInteraction().getBlockPosition())) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_INTERACT;
        }

        if (context.isPreview() && player.getWorld().isClient()) {
            itemStack.decrease(1);
            return BlockOperationResult.Type.CONSUME;
        }

        // compatible layer
        var originalItemStack = player.getItemStack(InteractionHand.MAIN);
        player.setItemStack(InteractionHand.MAIN, itemStack);
        var interacted = player.tryInteractionBlock(interaction);
        player.setItemStack(InteractionHand.MAIN, originalItemStack);

        if (!interacted) {
            return BlockOperationResult.Type.FAIL_UNKNOWN;
        }

        return BlockOperationResult.Type.SUCCESS;
    }

    @Override
    public BlockInteractOperationResult commit() {
        var inputs = blockState != null ? Collections.singletonList(blockState.getItem().getDefaultStack()) : Collections.<ItemStack>emptyList();
        var outputs = Collections.<ItemStack>emptyList();
        var result = interactBlock();

        if (getWorld().isClient() && getContext().isPreviewOnce() && result.success()) {
            var sound = SoundInstance.createBlock(getBlockState().getSoundSet().hitSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 2.0F, getBlockState().getSoundSet().pitch() * 0.8F, getInteraction().getBlockPosition().getCenter());
            getPlayer().getClient().getSoundManager().play(sound);
        }

        return new BlockInteractOperationResult(this, result, inputs, outputs);
    }

    @Override
    public BlockInteractOperation move(MoveContext moveContext) {
        return new BlockInteractOperation(world, player, context, storage, moveContext.move(interaction));
    }

    @Override
    public BlockInteractOperation mirror(MirrorContext mirrorContext) {
        return new BlockInteractOperation(world, player, context, storage, mirrorContext.mirror(interaction));
    }

    @Override
    public BlockInteractOperation revolve(RevolveContext revolveContext) {
        return null;
    }

    @Override
    public BlockInteractOperation refactor(RefactorContext refactorContext) {
        return this;
    }

}
