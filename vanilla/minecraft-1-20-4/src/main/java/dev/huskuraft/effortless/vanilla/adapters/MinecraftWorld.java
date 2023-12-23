package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.platform.Server;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.UUID;

class MinecraftWorld extends World {

    private final Level level;

    MinecraftWorld(Level level) {
        this.level = level;
    }

    public Level getRef() {
        return level;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftAdapter.adapt(getRef().getPlayerByUUID(uuid));
    }

    @Override
    public Server getServer() {
        if (getRef() instanceof ServerLevel) {
            return MinecraftAdapter.adapt(getRef().getServer());
        } else {
            return null;
        }
    }

    @Override
    public BlockData getBlockData(BlockPosition blockPosition) {
        return MinecraftAdapter.adapt(getRef().getBlockState(MinecraftAdapter.adapt(blockPosition)));
    }

    @Override
    public boolean isClient() {
        return getRef().isClientSide();
    }

    @Override
    public boolean isEnabledInAdventure(ItemStack itemStack, BlockPosition blockPosition) {
        return MinecraftAdapter.adapt(itemStack).hasAdventureModePlaceTagForBlock(getRef().registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(getRef(), MinecraftAdapter.adapt(blockPosition), false));
    }

    @Override
    public boolean isBlockPlaceable(BlockPosition blockPosition) {
        return getRef().getBlockState(MinecraftAdapter.adapt(blockPosition)).canBeReplaced();
    }

    @Override
    public boolean isBlockBreakable(BlockPosition blockPosition) {
        return !getRef().getBlockState(MinecraftAdapter.adapt(blockPosition)).is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    @Override
    public boolean placeBlock(
            Player player,
            BlockInteraction interaction,
            BlockData blockData,
            ItemStack itemStack) {

        var levelRef = getRef();
        var playerRef = MinecraftAdapter.adapt(player);
        var originalItemStackRef = playerRef.getMainHandItem();
        var itemStackRef = MinecraftAdapter.adapt(itemStack);
//        var mainHandItemStackRef2 = mainHandItemStackRef.copy();
        var blockStateRef = MinecraftAdapter.adapt(blockData);
        var blockItemRef = (BlockItem) blockStateRef.getBlock().asItem();
        var blockHitResultRef = MinecraftAdapter.adapt(interaction);
        var blockPosRef = blockHitResultRef.getBlockPos();

        playerRef.setItemInHand(InteractionHand.MAIN_HAND, itemStackRef);

        if (!blockStateRef.getBlock().isEnabled(getRef().enabledFeatures()) || !getRef().getBlockState(blockPosRef).canBeReplaced(new BlockPlaceContext(playerRef, InteractionHand.MAIN_HAND, itemStackRef, blockHitResultRef))) {
            playerRef.setItemInHand(InteractionHand.MAIN_HAND, originalItemStackRef);
            return false;
        }

        playerRef.setItemInHand(InteractionHand.MAIN_HAND, originalItemStackRef);

        var innerContext = new BlockPlaceContext(playerRef, InteractionHand.MAIN_HAND, itemStackRef, blockHitResultRef);
        if (levelRef.setBlock(innerContext.getClickedPos(), blockStateRef, 11)) {
            playerRef.setItemInHand(InteractionHand.MAIN_HAND, originalItemStackRef);
            return false;
        }
        if (!playerRef.getAbilities().instabuild) {
            playerRef.getMainHandItem().shrink(1);
        }

        if (playerRef instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPosRef, itemStackRef);
        }
        playerRef.awardStat(Stats.ITEM_USED.get(blockItemRef));
        playerRef.setItemInHand(InteractionHand.MAIN_HAND, originalItemStackRef);
        return true;

    }

    @Override
    public boolean breakBlock(
            Player player,
            BlockInteraction interaction) {

        var levelRef = getRef();
        var blockPosRef = MinecraftAdapter.adapt(interaction.getBlockPosition());
        var blockState = levelRef.getBlockState(blockPosRef);
        var block = blockState.getBlock();
        var playerRef = MinecraftAdapter.adapt(player);
        var blockEntityRef = levelRef.getBlockEntity(blockPosRef);

        block.playerWillDestroy(levelRef, blockPosRef, blockState, playerRef);
        var fluidState = levelRef.getFluidState(blockPosRef);
        var removed = levelRef.setBlock(blockPosRef, fluidState.createLegacyBlock(), 11);
        if (removed) {
            block.destroy(levelRef, blockPosRef, blockState);
        }
        // server
        if (playerRef.isCreative()) {
            return true;
        }
        var itemStackRef = playerRef.getMainHandItem();
        var itemStackRef2 = itemStackRef.copy();
        var correctTool = playerRef.hasCorrectToolForDrops(blockState);
        itemStackRef.mineBlock(levelRef, blockState, blockPosRef, playerRef);
        if (removed && correctTool) {
            block.playerDestroy(levelRef, playerRef, blockPosRef, blockState, blockEntityRef, itemStackRef2);
            return true;
        }
        return false;
    }

}
