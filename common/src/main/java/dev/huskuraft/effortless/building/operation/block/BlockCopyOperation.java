package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.ItemStack;
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

public class BlockCopyOperation extends BlockOperation {

    public BlockCopyOperation(
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
    public BlockCopyOperationResult commit() {
        if (!context.extras().dimensionId().equals(getWorld().getDimensionId().location())) {
            return new BlockCopyOperationResult(this, BlockOperationResult.Type.FAIL_WORLD_INCORRECT_DIM, null, null);
        }

        var entityState = EntityState.get(player);
        EntityState.set(player, getEntityState());
        var oldBlockState = getBlockStateInWorld();
        var result = getWorld().isClient() ? BlockOperationResult.Type.CONSUME : BlockOperationResult.Type.SUCCESS;
        EntityState.set(player, entityState);

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(player.getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }
        var newBlockState = getBlockStateInWorld();

        return new BlockCopyOperationResult(this, result, oldBlockState, newBlockState);
    }

    @Override
    public Operation move(MoveContext moveContext) {
        return new BlockCopyOperation(world, player, context, storage, moveContext.move(interaction), entityState);
    }

    @Override
    public Operation mirror(MirrorContext mirrorContext) {
        if (!mirrorContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation();
        }
        return new BlockCopyOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(entityState));
    }

    @Override
    public Operation rotate(RotateContext rotateContext) {
        if (!rotateContext.isInBounds(getBlockPosition().getCenter())) {
            return new EmptyOperation();
        }
        return new BlockCopyOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(entityState));
    }

    @Override
    public Operation refactor(RefactorContext source) {
        return this;
    }

    @Override
    public Type getType() {
        return Type.COPY;
    }

    private ItemStack getItemStack() {
        return world.getBlockState(interaction.getBlockPosition()).getItem().getDefaultStack();
    }
}
