package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.core.fluid.Fluid;

public record MinecraftBucketItem(net.minecraft.world.item.BucketItem referenceValue) implements BucketItem {

    @Override
    public Fluid getContent() {
        return new MinecraftFluid(referenceValue().content);
    }

    @Override
    public boolean useContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction) {
        return referenceValue().emptyContents(player.reference(), world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction));
    }

    @Override
    public void useExtraContent(World world, Player player, BlockPosition blockPosition, ItemStack itemStack) {
        referenceValue().checkExtraContent(player.reference(), world.reference(), itemStack.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition));
    }

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
