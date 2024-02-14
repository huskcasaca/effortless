package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.RevolveContext;

public class BlockBreakOnClientOperation extends BlockBreakOperation {

    public BlockBreakOnClientOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction
    ) {
        super(world, player, context, storage, interaction);
    }

    @Override
    public BlockBreakOnClientOperationResult commit() {
        var result = super.commit();

        if (getContext().isPreviewOnce()) {
            if (result.result().success()) {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().breakSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 2.0F, getBlockState().getSoundSet().pitch() * 0.8F, getInteraction().getBlockPosition().getCenter());
                EffortlessClient.getInstance().getClient().getSoundManager().play(sound);
            } else {
                var sound = SoundInstance.createBlock(getBlockState().getSoundSet().hitSound(), (getBlockState().getSoundSet().volume() + 1.0F) / 8.0F, getBlockState().getSoundSet().pitch() * 0.5F, getInteraction().getBlockPosition().getCenter());
                EffortlessClient.getInstance().getClient().getSoundManager().play(sound);
            }
        }

        return new BlockBreakOnClientOperationResult(
                this,
                result.result(),
                result.inputs(),
                result.outputs()
        );
    }

    @Override
    public BlockBreakOnClientOperation move(MoveContext moveContext) {
        return new BlockBreakOnClientOperation(world, player, context, storage, moveContext.move(interaction));
    }

    @Override
    public BlockBreakOnClientOperation mirror(MirrorContext mirrorContext) {
        return new BlockBreakOnClientOperation(world, player, context, storage, mirrorContext.mirror(interaction));
    }

    @Override
    public BlockBreakOnClientOperation revolve(RevolveContext revolveContext) {
        return new BlockBreakOnClientOperation(world, player, context, storage, revolveContext.revolve(interaction));
    }

    @Override
    public BlockBreakOnClientOperation refactor(RefactorContext source) {
        return this;
    }

}
