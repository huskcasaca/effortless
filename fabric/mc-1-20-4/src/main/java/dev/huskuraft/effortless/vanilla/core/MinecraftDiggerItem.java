package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.DiggerItem;
import dev.huskuraft.effortless.api.core.InteractionResult;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.text.Text;

public record MinecraftDiggerItem(net.minecraft.world.item.DiggerItem refs) implements DiggerItem {

    @Override
    public ItemStack getDefaultStack() {
        return new MinecraftItem(refs).getDefaultStack();
    }

    @Override
    public Block getBlock() {
        return new MinecraftItem(refs).getBlock();
    }

    @Override
    public ResourceLocation getId() {
        return new MinecraftItem(refs).getId();
    }

    @Override
    public InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction) {
        return new MinecraftItem(refs).useOnBlock(player, blockInteraction);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return new MinecraftItem(refs).isCorrectToolForDrops(blockState);
    }

    @Override
    public int getMaxStackSize() {
        return new MinecraftItem(refs).getMaxStackSize();
    }

    @Override
    public int getMaxDamage() {
        return new MinecraftItem(refs).getMaxDamage();
    }

    @Override
    public boolean mineBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return new MinecraftItem(refs).mineBlock(world, player, blockPosition, blockState, itemStack);
    }

    @Override
    public Text getName(ItemStack itemStack) {
        return new MinecraftItem(refs).getName(itemStack);
    }

}
