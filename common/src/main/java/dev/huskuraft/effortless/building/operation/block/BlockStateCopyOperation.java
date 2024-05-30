package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.empty.EmptyOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public class BlockStateCopyOperation extends BlockOperation {

    public BlockStateCopyOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            EntityState entityState
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()), entityState);
    }

    protected BlockOperationResultType saveBlock() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return BlockOperationResultType.FAIL_WORLD_INCORRECT_DIM;
        }
        if (getBlockState() == null) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_NULL;
        }
        if (!context.configs().constraintConfig().allowCopyPasteStructures()) {
            return BlockOperationResultType.FAIL_COPY_NO_PERMISSION;
        }
        if (getBlockState().isAir()) {
            return BlockOperationResultType.FAIL_BLOCK_STATE_AIR;
        }
        if (!context.configs().constraintConfig().whitelistedItems().isEmpty() && !context.configs().constraintConfig().whitelistedItems().contains(getBlockState().getItem().getId())) {
            return BlockOperationResultType.FAIL_COPY_BLACKLISTED;
        }
        if (!context.configs().constraintConfig().blacklistedItems().isEmpty() && context.configs().constraintConfig().blacklistedItems().contains(getBlockState().getItem().getId())) {
            return BlockOperationResultType.FAIL_COPY_BLACKLISTED;
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
        if (world.isClient()) {
            return BlockOperationResultType.CONSUME;
        }
        return BlockOperationResultType.SUCCESS;
    }

    @Override
    public BlockStateCopyOperationResult commit() {
        var entityStateBeforeOp = EntityState.get(getPlayer());
        var blockStateBeforeOp = getBlockStateInWorld();
        EntityState.set(getPlayer(), getEntityState());
        var result = saveBlock();
        EntityState.set(getPlayer(), entityStateBeforeOp);
        var blockStateAfterOp = getBlockStateInWorld();

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(getPlayer().getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }

        return new BlockStateCopyOperationResult(this, result, blockStateBeforeOp, blockStateAfterOp);
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockStateCopyOperation(world, player, context, storage, moveContext.move(interaction), entityState);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateCopyOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(entityState));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateCopyOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(entityState));
    }

    @Override
    public Operation refactor(RefactorContext source) {
        return this;
    }

    @Override
    public Type getType() {
        return Type.COPY;
    }
}
