package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.Operation;

public abstract class BlockOperation implements Operation {

    protected final World world;
    protected final Player player;
    protected final Context context;
    protected final Storage storage;
    protected final BlockInteraction interaction;
    protected final BlockState blockState;
    protected final BlockEntity blockEntity;
    protected final Extras extras;

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            BlockState blockState,
            BlockEntity blockEntity,
            Extras extras
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        this.extras = extras;
    }

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            BlockState blockState,
            Extras extras
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = blockState;
        this.blockEntity = null;
        this.extras = extras;
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

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public Extras getExtras() {
        return getContext().extras().extras();
    }

    public BlockInteraction getInteraction() {
        return interaction;
    }

    public BlockPosition getBlockPosition() {
        return getInteraction().getBlockPosition();
    }

    public BlockPosition getRelativeBlockPosition() {
        return getBlockPosition().relative(getInteraction().getDirection());
    }

    public InteractionHand getHand() {
        return InteractionHand.MAIN;
    }

    public boolean isInBorderBound() {
        return getWorld().getWorldBorder().isInBounds(getBlockPosition());
    }

    public boolean isInHeightBound() {
        return getBlockPosition().y() >= getWorld().getMinBuildHeight() && getBlockPosition().y() <= getWorld().getMaxBuildHeight();
    }

    public abstract Type getType();

    public BlockState getBlockStateInWorld() {
        return getWorld().getBlockState(getBlockPosition());
    }

    public BlockEntity getBlockEntityInWorld() {
        return getWorld().getBlockEntity(getBlockPosition());
    }

    public BlockEntity getBlockEntityInWorldCopied() {
        return getWorld().getBlockEntityCopied(getBlockPosition());
    }

    public enum Type {
        UPDATE,
        INTERACT,
        COPY
    }

}
