package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.core.BlockInteraction;

public abstract class BlockOperation extends TransformableOperation {

    protected final World world;
    protected final Player player;
    protected final Context context;
    protected final Storage storage;
    protected final BlockInteraction interaction;

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
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

    public BlockData getBlockData() {
        return null;
    }

    public BlockInteraction getInteraction() {
        return interaction;
    }

}
