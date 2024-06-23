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
            Extras extras
    ) {
        super(world, player, context, storage, interaction, extras);
    }

    protected BlockOperationResultType interactBlock() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return BlockOperationResultType.FAIL_WORLD_INCORRECT_DIM;
        }
        if (player.getGameMode().isSpectator()) {
            return BlockOperationResultType.FAIL_PLAYER_GAME_MODE;
        }
        if (!isInBorderBound()) {
            return BlockOperationResultType.FAIL_WORLD_BORDER;
        }
        if (!isInHeightBound()) {
            return BlockOperationResultType.FAIL_WORLD_HEIGHT;
        }
        if (getBlockState() == null) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_NULL;
        }

        if (!context.configs().constraintConfig().allowInteractBlocks()) {
            return BlockOperationResultType.FAIL_INTERACT_NO_PERMISSION;
        }
        if (!getBlockState().isAir()) {
            if (!context.configs().constraintConfig().whitelistedItems().isEmpty() && !context.configs().constraintConfig().whitelistedItems().contains(getBlockState().getItem().getId())) {
                return BlockOperationResultType.FAIL_INTERACT_BLACKLISTED;
            }
            if (!context.configs().constraintConfig().blacklistedItems().isEmpty() && context.configs().constraintConfig().blacklistedItems().contains(getBlockState().getItem().getId())) {
                return BlockOperationResultType.FAIL_INTERACT_BLACKLISTED;
            }
        }

        var itemStackToUse = storage.search(player.getItemStack(getHand()).getItem()).orElse(Items.AIR.item().getDefaultStack());

        if (context.isPreviewType() || context.isBuildClientType()) {
            itemStackToUse.decrease(1);
            return BlockOperationResultType.CONSUME;
        }

        if (context.isBuildType()) {
            var itemStackBeforeInteract = player.getItemStack(getHand());
//        if (!(itemStackBeforeInteract.getItem() instanceof BucketItem) && blockState.isAir()) {
//            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
//        }
            if (itemStackToUse.isDamageableItem() && itemStackToUse.getDurabilityLeft() <= context.getReservedToolDurability()) {
                return BlockOperationResultType.FAIL_INTERACT_TOOL_INSUFFICIENT;
            }
            player.setItemStack(getHand(), itemStackToUse);
            var interacted = getBlockStateInWorld().use(player, interaction).consumesAction();
            if (!interacted) {
                interacted = player.getItemStack(interaction.getHand()).getItem().useOnBlock(player, interaction).consumesAction();
                if (interacted && !world.isClient()) {
                    player.awardStat(StatTypes.ITEM_USED.get(itemStackToUse.getItem()));
                }
            }
            player.setItemStack(getHand(), itemStackBeforeInteract);
            if (!interacted) {
                return BlockOperationResultType.FAIL_UNKNOWN;
            }
        }

        return BlockOperationResultType.SUCCESS;
    }


    @Override
    public BlockInteractOperationResult commit() {
        var entityExtrasBeforeOp = Extras.get(getPlayer());
        var blockStateBeforeOp = getBlockStateInWorld();
        var entityTagBeforeOp = getEntityTagInWorld();
        Extras.set(getPlayer(), getExtras());
        var result = interactBlock();
        Extras.set(getPlayer(), entityExtrasBeforeOp);

        if (getContext().isBuildClientType() && getBlockPosition().toVector3d().distance(getPlayer().getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }
        var blockStateAfterOp = getBlockStateInWorld();
        var entityTagAfterOp = getEntityTagInWorld();
        return new BlockInteractOperationResult(this, result, blockStateBeforeOp, blockStateAfterOp, entityTagBeforeOp, entityTagAfterOp);
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockInteractOperation(world, player, context, storage, moveContext.move(interaction), extras);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockInteractOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(extras));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockInteractOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(extras));
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
