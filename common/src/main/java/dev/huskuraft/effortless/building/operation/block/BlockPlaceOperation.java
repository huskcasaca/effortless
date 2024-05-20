package dev.huskuraft.effortless.building.operation.block;

import java.util.List;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public class BlockPlaceOperation extends BlockOperation {

    public BlockPlaceOperation(
            World world,
            Player player,
            Context context,
            Storage storage,
            BlockInteraction interaction,
            BlockState blockState
    ) {
        super(world, player, context, storage, interaction, blockState);
    }


    @Override
    public BlockPlaceOperationResult commit() {
        var inputs = List.of(ItemStack.empty());
        if (blockState != null) {
            inputs = List.of(blockState.getItem().getDefaultStack());
        }
        var outputs = List.of(ItemStack.empty());
        if (getWorld().getBlockState(getBlockPosition()) != null) {
            outputs = List.of(getWorld().getBlockState(getBlockPosition()).getItem().getDefaultStack());
        }
        var result = placeBlock();

        if (getWorld().isClient() && getContext().isPreviewOnceType() && getBlockPosition().toVector3d().distance(player.getEyePosition()) <= 32) {
            getPlayer().getClient().getParticleEngine().crack(getBlockPosition(), getInteraction().getDirection());
        }

        return new BlockPlaceOperationResult(this, result, inputs, outputs);
    }

    @Override
    public BlockPlaceOperation move(MoveContext moveContext) {
        return new BlockPlaceOperation(world, player, context, storage, moveContext.move(interaction), blockState);
    }

    @Override
    public BlockPlaceOperation mirror(MirrorContext mirrorContext) {
        return new BlockPlaceOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(blockState));
    }

    @Override
    public BlockPlaceOperation rotate(RotateContext rotateContext) {
        return new BlockPlaceOperation(world, player, context, storage, rotateContext.rotate(interaction), rotateContext.rotate(blockState));
    }

    @Override
    public BlockOperation refactor(RefactorContext refactorContext) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, refactorContext.refactor(player, getInteraction()));
    }

    @Override
    public Type getType() {
        return Type.PLACE;
    }
}
