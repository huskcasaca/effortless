package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.TransformableOperation;

public abstract class BlockOperation extends TransformableOperation {

    protected final World world;
    protected final Player player;
    protected final Context context;
    protected final Storage storage;
    protected final BlockInteraction interaction;
    protected final BlockState blockState;

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            BlockState blockState
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = blockState;
    }

    @Override
    public BlockPosition locate() {
        return getInteraction().getBlockPosition();
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public Context getContext() {
        return context;
    }

    public Storage getStorage() {
        return storage;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public BlockInteraction getInteraction() {
        return interaction;
    }

}
