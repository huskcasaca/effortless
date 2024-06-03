package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ContainerBlockEntity;
import dev.huskuraft.effortless.api.core.ItemStack;

public record MinecraftContainerBlockEntity(net.minecraft.world.level.block.entity.BaseContainerBlockEntity refs) implements ContainerBlockEntity {

    @Override
    public ItemStack getItem(int index) {
        return new MinecraftContainer(refs).getItem(index);
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        new MinecraftContainer(refs).setItem(index, itemStack);
    }

    @Override
    public int getContainerSize() {
        return new MinecraftContainer(refs).getContainerSize();
    }

}
