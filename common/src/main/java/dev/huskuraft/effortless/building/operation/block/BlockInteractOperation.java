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

    @Override
    public BlockInteractOperationResult commit() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return new BlockInteractOperationResult(this, BlockOperationResult.Type.FAIL_WORLD_INCORRECT_DIM, null, null);
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
            return new EmptyOperation();
        }
        return new BlockInteractOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(entityState));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation();
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
