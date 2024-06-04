package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.tag.RecordTag;
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
    protected final RecordTag entityTag;
    protected final Extras extras;

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            BlockState blockState,
            RecordTag entityTag,
            Extras extras
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = blockState;
        this.entityTag = entityTag;
        this.extras = extras;
    }

    protected BlockOperation(
            World world,
            Player player,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            Extras extras
    ) {
        this.world = world;
        this.player = player;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = world.getBlockState(interaction.getBlockPosition());
        var blockEntity = world.getBlockEntity(interaction.getBlockPosition());
        if (blockEntity != null) {
            // TODO: 4/6/24 clear position & id tags in 1.17.1
            this.entityTag = blockEntity.getTag();
        } else {
            this.entityTag = null;
        }
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

    public RecordTag getEntityTag() {
        return entityTag;
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

    public RecordTag getEntityTagInWorld() {
        var blockEntity = getWorld().getBlockEntity(getBlockPosition());
        if (blockEntity == null) {
            return null;
        }
        return blockEntity.getTag();
    }

    public enum Type {
        UPDATE,
        INTERACT,
        COPY
    }

}
