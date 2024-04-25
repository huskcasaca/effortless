package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import net.minecraft.core.DefaultedRegistry;

public class MinecraftItem implements Item {

    private final net.minecraft.world.item.Item reference;

    public MinecraftItem(net.minecraft.world.item.Item reference) {
        this.reference = reference;
    }

    public static Item ofNullable(net.minecraft.world.item.Item reference) {
        return reference == null ? null : reference instanceof net.minecraft.world.item.BlockItem blockItem ? new MinecraftBlockItem(blockItem) : new MinecraftItem(reference);
    }

    @Override
    public net.minecraft.world.item.Item referenceValue() {
        return reference;
    }

    @Override
    public ItemStack getDefaultStack() {
        return new MinecraftItemStack(reference.getDefaultInstance());
    }

    @Override
    public Block getBlock() {
        return new MinecraftBlock(net.minecraft.world.level.block.Block.byItem(reference));
    }

    @Override
    public ResourceLocation getId() {
        var minecraftResourceLocation = DefaultedRegistry.ITEM.getKey(reference);
        return new MinecraftResourceLocation(minecraftResourceLocation);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftItem item && reference.equals(item.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
