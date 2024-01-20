package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Resource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;

public class MinecraftItem extends Item {

    private final net.minecraft.world.item.Item reference;

    MinecraftItem(net.minecraft.world.item.Item reference) {
        this.reference = reference;
    }

    public static Item fromMinecraft(net.minecraft.world.item.Item item) {
        return new MinecraftItem(item);
    }

    public static net.minecraft.world.item.Item toMinecraft(Item item) {
        return ((MinecraftItem) item).reference;
    }

    @Override
    public ItemStack getDefaultStack() {
        return MinecraftItemStack.fromMinecraft(reference.getDefaultInstance());
    }

    @Override
    public boolean isBlockItem() {
        return reference instanceof BlockItem;
    }

    @Override
    public Resource getId() {
        return MinecraftResource.fromMinecraftResource(BuiltInRegistries.ITEM.getKey(reference));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var that = (MinecraftItem) o;

        return reference.equals(that.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
