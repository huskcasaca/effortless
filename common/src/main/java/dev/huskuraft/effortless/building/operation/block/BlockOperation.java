package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionHand;
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
        return getBlockPosition();
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

    public boolean destroyBlock() {
        var blockState = getWorld().getBlockState(getBlockPosition());
        var blockEntity = getWorld().getBlockEntity(getBlockPosition());
        var itemInHand = getPlayer().getItemStack(InteractionHand.MAIN);
        var itemInHandCopy = itemInHand.copy();

        blockState.getBlock().playerDestroyStart(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);

        var removed = getWorld().removeBlock(getBlockPosition(), false);
        if (removed) {
            getWorld().getBlockState(getBlockPosition()).getBlock().destroy(getWorld(), getBlockPosition(), blockState);
        }
        if (getPlayer().getGameMode().isCreative()) {
            return true;
        }
        var hasCorrectToolForDrops = !blockState.requiresCorrectToolForDrops() || itemInHand.isCorrectToolForDrops(blockState);
        itemInHand.mineBlock(getWorld(), getPlayer(), getBlockPosition(), blockState);
        if (removed && hasCorrectToolForDrops) {
            blockState.getBlock().playerDestroy(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);
        }
        return true;
    }

    public boolean useItem(BlockInteraction interaction) {
        return getWorld().getBlockState(interaction.getBlockPosition()).use(getPlayer(), interaction).consumesAction() || getPlayer().getItemStack(interaction.getHand()).getItem().useOnBlock(getPlayer(), interaction).consumesAction();
    }


}
