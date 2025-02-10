package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.World;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.session.Session;

public abstract class BlockOperation implements Operation {

    protected final Session session;
    protected final Context context;
    protected final Storage storage;
    protected final BlockInteraction interaction;
    protected final BlockState blockState;
    protected final RecordTag entityTag;
    protected final Extras extras;

    protected BlockOperation(
            Session session,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            BlockState blockState,
            RecordTag entityTag,
            Extras extras
    ) {
        this.session = session;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = blockState;
        this.entityTag = entityTag;
        this.extras = extras;
    }

    protected BlockOperation(
            Session session,
            Context context,
            Storage storage, // for preview
            BlockInteraction interaction,
            Extras extras
    ) {
        this.session = session;
        this.context = context;
        this.storage = storage;
        this.interaction = interaction;
        this.blockState = session.getWorld().getBlockState(interaction.getBlockPosition());
        var blockEntity = session.getWorld().getBlockEntity(interaction.getBlockPosition());
        if (blockEntity != null) {
            // TODO: 4/6/24 clear position & id tags in 1.17.1
            this.entityTag = blockEntity.getTag();
        } else {
            this.entityTag = null;
        }
        this.extras = extras;
    }

    public final Session getSession() {
        return session;
    }

    public final World getWorld() {
        return session.getWorld();
    }

    public final Player getPlayer() {
        return session.getPlayer();
    }

    public final Context getContext() {
        return context;
    }

    public final Storage getStorage() {
        return storage;
    }

    public final BlockState getBlockState() {
        return blockState;
    }

    public final RecordTag getEntityTag() {
        return entityTag;
    }

    public final Extras getExtras() {
        return getContext().extras().extras();
    }

    public final BlockInteraction getInteraction() {
        return interaction;
    }

    public final BlockPosition getBlockPosition() {
        return getInteraction().getBlockPosition();
    }

    public final BlockPosition getRelativeBlockPosition() {
        return getBlockPosition().relative(getInteraction().getDirection());
    }

    public final InteractionHand getHand() {
        return InteractionHand.MAIN;
    }

    public final boolean isInBorderBound() {
        return getWorld().getWorldBorder().isInBounds(getBlockPosition());
    }

    public final boolean isInHeightBound() {
        return getBlockPosition().y() >= getWorld().getMinBuildHeight() && getBlockPosition().y() <= getWorld().getMaxBuildHeight();
    }

    public final boolean allowInteraction() {
        return getSession().getInterceptors().stream().allMatch(interceptor -> interceptor.allowInteraction(getPlayer(), getWorld(), getBlockPosition()));
    }

    public abstract Type getType();

    public final BlockState getBlockStateInWorld() {
        return getWorld().getBlockState(getBlockPosition());
    }

    public final BlockEntity getBlockEntityInWorld() {
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
