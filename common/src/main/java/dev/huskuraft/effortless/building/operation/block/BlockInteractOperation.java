package dev.huskuraft.effortless.building.operation.block;

import java.util.Collections;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
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
            BlockInteraction interaction
    ) {
        super(world, player, context, storage, interaction, world.getBlockState(interaction.getBlockPosition()));
    }

    @Override
    public BlockInteractOperationResult commit() {
        var inputs = blockState != null ? Collections.singletonList(blockState.getItem().getDefaultStack()) : Collections.<ItemStack>emptyList();
        var outputs = Collections.<ItemStack>emptyList();

        var entityState = EntityState.get(player);
        EntityState.set(player, getEntityState());
        var result = interactBlock();
        EntityState.set(player, entityState);

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(player.getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
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
    public BlockInteractOperation rotate(RotateContext rotateContext) {
        return new BlockInteractOperation(world, player, context, storage, rotateContext.rotate(interaction));
    }

    @Override
    public BlockInteractOperation refactor(RefactorContext refactorContext) {
        return this;
    }

    @Override
    public Type getType() {
        return Type.INTERACT;
    }
}
