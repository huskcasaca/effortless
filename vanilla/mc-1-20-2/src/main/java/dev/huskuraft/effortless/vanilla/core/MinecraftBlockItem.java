package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionResult;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import net.minecraft.world.item.context.BlockPlaceContext;

public record MinecraftBlockItem(net.minecraft.world.item.BlockItem referenceValue) implements BlockItem {

    @Override
    public ItemStack getDefaultStack() {
        return new MinecraftItem(referenceValue()).getDefaultStack();
    }

    @Override
    public Block getBlock() {
        return new MinecraftItem(referenceValue()).getBlock();
    }

    @Override
    public ResourceLocation getId() {
        return new MinecraftItem(referenceValue()).getId();
    }

    @Override
    public InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction) {
        return new MinecraftItem(referenceValue()).useOnBlock(player, blockInteraction);
    }

    @Override
    public InteractionResult placeOnBlock(Player player, BlockInteraction blockInteraction) {
        return MinecraftConvertor.toPlatformInteractionResult(referenceValue().place(new BlockPlaceContext(player.reference(), MinecraftConvertor.toPlatformInteractionHand(blockInteraction.getHand()), player.getItemStack(blockInteraction.getHand()).reference(), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction))));
    }

    @Override
    public boolean setBlockInWorld(Player player, BlockInteraction blockInteraction, BlockState blockState) {
        return referenceValue().placeBlock(new BlockPlaceContext(player.reference(), MinecraftConvertor.toPlatformInteractionHand(blockInteraction.getHand()), player.getItemStack(blockInteraction.getHand()).reference(), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction)), blockState.reference());
    }

    @Override
    public boolean updateBlockEntityTag(World world, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return referenceValue().updateCustomBlockEntityTag(MinecraftConvertor.toPlatformBlockPosition(blockPosition), ((MinecraftWorld) world).referenceValue(), (net.minecraft.world.entity.player.Player) null, ((MinecraftItemStack) itemStack).referenceValue(), ((MinecraftBlockState) blockState).referenceValue());
    }

    @Override
    public BlockState updateBlockStateFromTag(World world, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return MinecraftBlockState.ofNullable(referenceValue().updateBlockStateFromTag(MinecraftConvertor.toPlatformBlockPosition(blockPosition), world.reference(), itemStack.reference(), blockState.reference()));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return new MinecraftItem(referenceValue()).isCorrectToolForDrops(blockState);
    }

    @Override
    public int getMaxStackSize() {
        return new MinecraftItem(referenceValue()).getMaxStackSize();
    }

    @Override
    public int getMaxDamage() {
        return new MinecraftItem(referenceValue()).getMaxDamage();
    }

    @Override
    public boolean mineBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return new MinecraftItem(referenceValue()).mineBlock(world, player, blockPosition, blockState, itemStack);
    }

}
