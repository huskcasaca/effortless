package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;

public class BlockPlaceOnClientOperation extends BlockPlaceOperation {

    public BlockPlaceOnClientOperation(
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
    public BlockPlaceOnClientOperationResult commit() {
        var result = super.commit();

        if (getContext().isPreviewOnce() && result.result().success()) {
            var sound = SoundInstance.createBlock(getBlockState().getSoundSet().placeSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 2.0F, getBlockState().getSoundSet().pitch() * 0.8F, getInteraction().getBlockPosition().getCenter());
            EffortlessClient.getInstance().getClient().getSoundManager().play(sound);
        }

        return new BlockPlaceOnClientOperationResult(
                this,
                result.result(),
                result.inputs(),
                result.outputs()
        );
    }

    @Override
    public BlockPlaceOnClientOperation move(MoveContext moveContext) {
        return new BlockPlaceOnClientOperation(world, player, context, storage, moveContext.move(interaction), blockState);
    }

    @Override
    public BlockPlaceOnClientOperation mirror(MirrorContext mirrorContext) {
        return new BlockPlaceOnClientOperation(world, player, context, storage, mirrorContext.mirror(interaction), mirrorContext.mirror(blockState));
    }

    @Override
    public BlockPlaceOnClientOperation revolve(RevolveContext revolveContext) {
        return null;
    }

    @Override
    public BlockPlaceOnClientOperation refactor(RefactorContext refactorContext) {
        return new BlockPlaceOnClientOperation(world, player, context, storage, interaction, refactorContext.refactor(player, getInteraction()));
    }

}
