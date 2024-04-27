package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;

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
}
