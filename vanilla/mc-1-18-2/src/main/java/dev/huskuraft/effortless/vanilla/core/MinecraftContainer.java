package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Container;
import dev.huskuraft.effortless.api.core.ItemStack;

public record MinecraftContainer(net.minecraft.world.Container refs) implements Container {

    @Override
    public ItemStack getItem(int index) {
        return new MinecraftItemStack(refs.getItem(index));
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        refs.setItem(index, itemStack.reference());
    }

    @Override
    public int getContainerSize() {
        return refs.getContainerSize();
    }

}
