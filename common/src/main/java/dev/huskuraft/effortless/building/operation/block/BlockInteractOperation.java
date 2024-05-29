package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.StatTypes;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public class BlockInteractOperation extends BlockOperation {

    public BlockInteractOperation(
            World world,
            Player player,
            Context context,
            Storage storage,
            BlockInteraction interaction,
            EntityState entityState
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()), entityState);
    }

    protected BlockOperationResultType interactBlock() {
        if (getBlockState() == null) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_NULL;
        }
        if (!context.configs().constraintConfig().allowInteractBlocks()) {
            return BlockOperationResultType.FAIL_INTERACT_NO_PERMISSION;
        }

        if (!context.configs().constraintConfig().whitelistedItems().isEmpty() && !context.configs().constraintConfig().whitelistedItems().contains(getBlockState().getItem().getId())) {
            return BlockOperationResultType.FAIL_PLACE_BLACKLISTED;
        }

        if (!context.configs().constraintConfig().blacklistedItems().isEmpty() && context.configs().constraintConfig().blacklistedItems().contains(getBlockState().getItem().getId())) {
            return BlockOperationResultType.FAIL_PLACE_BLACKLISTED;
        }

        if (player.getGameMode().isSpectator()) {
            return BlockOperationResultType.FAIL_PLAYER_GAME_MODE;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResultType.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResultType.FAIL_WORLD_HEIGHT;
        }

        // action permission
        var selectedItemStack = storage.search(player.getItemStack(getHand()).getItem()).orElse(Items.AIR.item().getDefaultStack());

//        if (selectedItemStack == null) {
//            return BlockOperationResult.Type.FAIL_PLACE_ITEM_INSUFFICIENT;
//        }
//
//
//        if (!selectedItemStack.getItem().isBlockItem()) {
//            return BlockOperationResult.Type.FAIL_PLACE_ITEM_NOT_BLOCK;
//        }

        if (context.isPreviewType() && player.getWorld().isClient()) {
            selectedItemStack.decrease(1);
            return BlockOperationResultType.CONSUME;
        }

        if (world.isClient()) {
            return BlockOperationResultType.CONSUME;
        }
        // compatible layer
        var originalItemStack = player.getItemStack(getHand());

//        if (!(originalItemStack.getItem() instanceof BucketItem) && blockState.isAir()) {
//            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
//        }

        if (selectedItemStack.isDamageableItem() && selectedItemStack.getRemainingDamage() <= context.getReservedToolDurability()) {
            return BlockOperationResultType.FAIL_PLACE_ITEM_INSUFFICIENT;
        }

        player.setItemStack(getHand(), selectedItemStack);

        var interacted = getBlockStateInWorld().use(player, interaction).consumesAction();
        if (!interacted) {
            interacted = player.getItemStack(interaction.getHand()).getItem().useOnBlock(player, interaction).consumesAction();
            if (interacted && !world.isClient()) {
                player.awardStat(StatTypes.ITEM_USED.get(selectedItemStack.getItem()));
            }
        }
        player.setItemStack(getHand(), originalItemStack);
        if (!interacted) {
            return BlockOperationResultType.FAIL_UNKNOWN;
        }

        return BlockOperationResultType.SUCCESS;
    }


    @Override
    public BlockInteractOperationResult commit() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return new BlockInteractOperationResult(this, BlockOperationResultType.FAIL_WORLD_INCORRECT_DIM, null, null);
        }

        var entityState = EntityState.get(player);
        var beforeBlockState = getBlockStateInWorld();
        EntityState.set(player, getEntityState());
        var result = interactBlock();
        EntityState.set(player, entityState);

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(player.getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }
        var afterBlockState = getBlockStateInWorld();
        return new BlockInteractOperationResult(this, result, beforeBlockState, afterBlockState);
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockInteractOperation(world, player, context, storage, moveContext.move(interaction), entityState);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockInteractOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(entityState));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockInteractOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(entityState));
    }

    @Override
    public Operation refactor(RefactorContext refactorContext) {
        return this;
    }

    @Override
    public Type getType() {
        return Type.INTERACT;
    }
}
