package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.StatTypes;
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

    public EntityState getEntityState() {
        return getContext().patternParams().interactState();
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

    protected boolean destroyBlockInternal() {
        var blockState = getWorld().getBlockState(getBlockPosition());
        var blockEntity = getWorld().getBlockEntity(getBlockPosition());
        var itemInHand = getPlayer().getItemStack(InteractionHand.MAIN);
        var itemInHandCopy = itemInHand.copy();

        blockState.getBlock().destroyStart(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);

        var removed = getWorld().removeBlock(getBlockPosition(), false);
        if (removed) {
            getWorld().getBlockState(getBlockPosition()).getBlock().destroy(getWorld(), getPlayer(), getBlockPosition(), blockState);
        }
        if (getPlayer().getGameMode().isCreative()) {
            return true;
        }
        var correctTool = !blockState.requiresCorrectToolForDrops() || itemInHand.isCorrectToolForDrops(blockState);
        itemInHand.mineBlock(getWorld(), getPlayer(), getBlockPosition(), blockState);
        if (removed && correctTool) {
            blockState.getBlock().destroyEnd(getWorld(), getPlayer(), getBlockPosition(), blockState, blockEntity, itemInHandCopy);
        }
        return removed;
    }

    protected BlockOperationResult.Type destroyBlockCheckOnly() {

        // config permission
        if (!context.customParams().generalConfig().allowBreakBlocks()) {
            return BlockOperationResult.Type.FAIL_CONFIG_BREAK_PERMISSION;
        }

        if (!context.customParams().generalConfig().whitelistedItems().isEmpty() && !context.customParams().generalConfig().whitelistedItems().contains(getBlockItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        if (!context.customParams().generalConfig().blacklistedItems().isEmpty() && context.customParams().generalConfig().blacklistedItems().contains(getBlockItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        // game mode permission
        if (player.getGameMode().isBlockPlacingRestricted()) { // move
            return BlockOperationResult.Type.FAIL_PLAYER_GAME_MODE;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_HEIGHT;
        }

        // world permission
        if (!player.getGameMode().isCreative() && !player.getWorld().getBlockState(getBlockPosition()).isDestroyable()) {
            return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
        }

        if (world.getBlockState(getBlockPosition()).isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }

        var blockState = world.getBlockState(getBlockPosition());
        var reservedDurability = getContext().getReservedToolDurability();
        var useCorrectTool = !player.getGameMode().isCreative() && context.useCorrectTool();
        var correctTool = getStorage().contents().stream().filter(itemStack -> itemStack.getItem().isCorrectToolForDrops(blockState)).filter(itemStack -> !itemStack.isDamageableItem() || itemStack.getRemainingDamage() > reservedDurability).findFirst();

        if (useCorrectTool && correctTool.isEmpty()) {
            return BlockOperationResult.Type.FAIL_TOOL_INSUFFICIENT;
        }

        if (context.isPreviewType() && world.isClient()) {
            if (useCorrectTool) {
                correctTool.get().damageBy(player, 1);
            }
            return BlockOperationResult.Type.CONSUME;
        }
        return BlockOperationResult.Type.CONSUME;
    }


    protected BlockOperationResult.Type destroyBlock() {

        var check = destroyBlockCheckOnly();
        if (check.fail()) {
            return check;
        }
        // build type
        var useCorrectTool = !player.getGameMode().isCreative() && context.useCorrectTool();
        var reservedDurability = getContext().getReservedToolDurability();
        var correctTool = getStorage().contents().stream().filter(itemStack -> itemStack.getItem().isCorrectToolForDrops(blockState)).filter(itemStack -> !itemStack.isDamageableItem() || itemStack.getRemainingDamage() > reservedDurability).findFirst();

        if (world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }

        var oldItem = player.getItemStack(InteractionHand.MAIN);
        if (useCorrectTool) {
            player.setItemStack(InteractionHand.MAIN, correctTool.get());
        }

        var destroyed = destroyBlockInternal();

        if (useCorrectTool) {
            player.setItemStack(InteractionHand.MAIN, oldItem);
        }

        if (destroyed) {
            return BlockOperationResult.Type.SUCCESS;
        } else {
            return BlockOperationResult.Type.FAIL_UNKNOWN;
        }
    }

    private Item getBlockItem() {
        return world.getBlockState(interaction.getBlockPosition()).getItem();
    }


    protected BlockOperationResult.Type placeBlock() {

        if (blockState == null) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_NULL;
        }

        // config permission
        if (!context.customParams().generalConfig().allowPlaceBlocks()) {
            return BlockOperationResult.Type.FAIL_CONFIG_PLACE_PERMISSION;
        }

        if (!context.customParams().generalConfig().whitelistedItems().isEmpty() && !context.customParams().generalConfig().whitelistedItems().contains(blockState.getItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        if (!context.customParams().generalConfig().blacklistedItems().isEmpty() && context.customParams().generalConfig().blacklistedItems().contains(blockState.getItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        // game mode permission
        if (player.getGameMode().isSpectator()) {
            return BlockOperationResult.Type.FAIL_PLAYER_GAME_MODE;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_HEIGHT;
        }

        // action permission
        var itemStack = storage.search(blockState.getItem()).orElse(null);

        if (itemStack == null || itemStack.isEmpty()) {
            return BlockOperationResult.Type.FAIL_ITEM_INSUFFICIENT;
        }

        if (itemStack.isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }

        if (!(itemStack.getItem() instanceof BlockItem blockItem)) {
            return BlockOperationResult.Type.FAIL_ITEM_NOT_BLOCK;
        }

        if (!player.getWorld().getBlockState(getBlockPosition()).canBeReplaced(player, getInteraction())) {
            if (context.replaceMode().isReplace()) {
                var destroyCheck = destroyBlockCheckOnly();
                if (destroyCheck.fail()) {
                    return destroyCheck;
                }
            } else {
                return BlockOperationResult.Type.FAIL_PLAYER_CANNOT_BREAK;
            }
        }

        if (context.isPreviewType() && player.getWorld().isClient()) {
            itemStack.decrease(1);
            return BlockOperationResult.Type.CONSUME;
        }

        if (world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }

        // build type
        if (!player.getWorld().getBlockState(getBlockPosition()).canBeReplaced(player, getInteraction())) {
            if (context.replaceMode().isReplace()) {
                var destroyResult = destroyBlock();
                if (!destroyResult.success()) {
                    return destroyResult;
                }
            }
        }

//        if (context.buildType() == BuildType.COMMAND) {
//            CommandManager.dispatch(new SetBlockCommand(getBlockState(), getBlockPosition(), SetBlockCommand.Mode.REPLACE));
//            return BlockOperationResult.Type.SUCCESS;
//        }

        var originalItemStack = player.getItemStack(getHand());
        player.setItemStack(getHand(), itemStack);
        var placed = false;
        if (context.useLegacyBlockPlace()) {
            placed = blockItem.placeOnBlock(player, getInteraction()).consumesAction();
        } else {
            placed = blockItem.setBlockOnly(getWorld(), getPlayer(), getInteraction(), getBlockState());
            if (placed /*&& getWorld().getBlockState(getBlockPosition()).getBlock() == */) {
                // FIXME: 19/5/24
//                blockItem.updateBlockStateFromTag(getWorld(), getBlockPosition(), getBlockState(), itemStack);
                blockItem.updateBlockEntityTag(getWorld(), getBlockPosition(), getBlockState(), itemStack);
                blockItem.getBlock().place(getWorld(), getPlayer(), getBlockPosition(), getBlockState(), itemStack);
            }
            if (placed && !getPlayer().getGameMode().isCreative()) {
                itemStack.decrease(1);
            }
        }
        player.setItemStack(getHand(), originalItemStack);

        if (!placed) {
            return BlockOperationResult.Type.FAIL_UNKNOWN;
        }
        if (!world.isClient()) {
            player.awardStat(StatTypes.ITEM_USED.get(itemStack.getItem()));
        }

        // FIXME: 29/4/24
//        if (!world.getBlockState(getBlockPosition()).equals(blockState) && !world.setBlockAndUpdate(getBlockPosition(), blockState)) {
//            return BlockOperationResult.Type.FAIL_UNKNOWN;
//        }

        return BlockOperationResult.Type.SUCCESS;
    }

    protected BlockOperationResult.Type interactBlock() {

        if (blockState == null) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_NULL;
        }

        // config permission
        if (!context.customParams().generalConfig().allowInteractBlocks()) {
            return BlockOperationResult.Type.FAIL_CONFIG_INTERACT_PERMISSION;
        }

        if (!context.customParams().generalConfig().whitelistedItems().isEmpty() && !context.customParams().generalConfig().whitelistedItems().contains(blockState.getItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        if (!context.customParams().generalConfig().blacklistedItems().isEmpty() && context.customParams().generalConfig().blacklistedItems().contains(blockState.getItem().getId())) {
            return BlockOperationResult.Type.FAIL_CONFIG_BLACKLISTED;
        }

        // game mode permission
        if (player.getGameMode().isSpectator()) {
            return BlockOperationResult.Type.FAIL_PLAYER_GAME_MODE;
        }

        // world permission
        if (!isInBorderBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_BORDER;
        }

        if (!isInHeightBound()) {
            return BlockOperationResult.Type.FAIL_WORLD_HEIGHT;
        }

        // action permission
        var selectedItemStack = storage.search(player.getItemStack(getHand()).getItem()).orElse(Items.AIR.item().getDefaultStack());

//        if (selectedItemStack == null) {
//            return BlockOperationResult.Type.FAIL_ITEM_INSUFFICIENT;
//        }
//
//
//        if (!selectedItemStack.getItem().isBlockItem()) {
//            return BlockOperationResult.Type.FAIL_ITEM_NOT_BLOCK;
//        }

        if (context.isPreviewType() && player.getWorld().isClient()) {
            selectedItemStack.decrease(1);
            return BlockOperationResult.Type.CONSUME;
        }

        if (world.isClient()) {
            return BlockOperationResult.Type.CONSUME;
        }
        // compatible layer
        var originalItemStack = player.getItemStack(getHand());

        if (!(originalItemStack.getItem() instanceof BucketItem) && blockState.isAir()) {
            return BlockOperationResult.Type.FAIL_BLOCK_STATE_AIR;
        }

        if (selectedItemStack.isDamageableItem() && selectedItemStack.getRemainingDamage() <= context.getReservedToolDurability()) {
            return BlockOperationResult.Type.FAIL_ITEM_INSUFFICIENT;
        }

        player.setItemStack(getHand(), selectedItemStack);

        var interacted = getWorld().getBlockState(interaction.getBlockPosition()).use(player, interaction).consumesAction();
        if (!interacted) {
            interacted = player.getItemStack(interaction.getHand()).getItem().useOnBlock(player, interaction).consumesAction();
            if (interacted && !world.isClient()) {
                player.awardStat(StatTypes.ITEM_USED.get(selectedItemStack.getItem()));
            }
        }
        player.setItemStack(getHand(), originalItemStack);
        if (!interacted) {
            return BlockOperationResult.Type.FAIL_UNKNOWN;
        }

        return BlockOperationResult.Type.SUCCESS;
    }


    public boolean useItem(BlockInteraction interaction) {
        return getWorld().getBlockState(interaction.getBlockPosition()).use(getPlayer(), interaction).consumesAction() || getPlayer().getItemStack(interaction.getHand()).getItem().useOnBlock(getPlayer(), interaction).consumesAction();
    }

    public enum Type {
        BREAK,
        PLACE,
        INTERACT
    }

}
