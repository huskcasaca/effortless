package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Resource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;

class MinecraftItem extends Item {

    private final net.minecraft.world.item.Item item;

    MinecraftItem(net.minecraft.world.item.Item item) {
        this.item = item;
    }

    public net.minecraft.world.item.Item getRef() {
        return item;
    }

    @Override
    public ItemStack getDefaultStack() {
        return MinecraftAdapter.adapt(getRef().getDefaultInstance());
    }

    @Override
    public boolean isBlockItem() {
        return getRef() instanceof BlockItem;
    }

    @Override
    public Resource getId() {
        return MinecraftAdapter.adapt(BuiltInRegistries.ITEM.getKey(getRef()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var that = (MinecraftItem) o;

        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }
}
