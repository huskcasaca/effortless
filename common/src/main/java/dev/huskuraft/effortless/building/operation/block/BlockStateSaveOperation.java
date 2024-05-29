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

public class BlockStateSaveOperation extends BlockOperation {

    public BlockStateSaveOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            EntityState entityState
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()), entityState);
    }


    @Override
    public BlockStateSaveOperationResult commit() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return new BlockStateSaveOperationResult(this, BlockOperationResultType.FAIL_WORLD_INCORRECT_DIM, null, null);
        }

        var entityState = EntityState.get(player);
        EntityState.set(player, getEntityState());
        var oldBlockState = getBlockStateInWorld();
        var result = getWorld().isClient() ? BlockOperationResultType.CONSUME : BlockOperationResultType.SUCCESS;
        EntityState.set(player, entityState);

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(player.getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }
        var newBlockState = getBlockStateInWorld();

        return new BlockStateSaveOperationResult(this, result, oldBlockState, newBlockState);
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockStateSaveOperation(world, player, context, storage, moveContext.move(interaction), entityState);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateSaveOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(entityState));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation(context);
        }
        return new BlockStateSaveOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(entityState));
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
