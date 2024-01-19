package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.world.item.BlockItem;

class MinecraftItem implements Item {

    private final net.minecraft.world.item.Item reference;

    MinecraftItem(net.minecraft.world.item.Item reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.item.Item referenceValue() {
        return reference;
    }

    @Override
    public ItemStack getDefaultStack() {
        return MinecraftConvertor.fromPlatformItemStack(reference.getDefaultInstance());
    }

    @Override
    public boolean isBlockItem() {
        return reference instanceof BlockItem;
    }

    @Override
    public ResourceLocation getId() {
        return MinecraftConvertor.fromPlatformResourceLocation(DefaultedRegistry.ITEM.getKey(reference));
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
