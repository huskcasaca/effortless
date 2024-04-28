package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Inventory;
import dev.huskuraft.effortless.api.core.ItemStack;

public record MinecraftInventory(net.minecraft.world.entity.player.Inventory referenceValue) implements Inventory {

    @Override
    public List<ItemStack> getItems() {
        return referenceValue().items.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getArmors() {
        return referenceValue().armor.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getOffhand() {
        return referenceValue().offhand.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        referenceValue().setItem(index, itemStack.reference());
    }

    @Override
    public ItemStack getItem(int index) {
        return new MinecraftItemStack(referenceValue().getItem(index));
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        return referenceValue().add(itemStack.reference());
    }

    @Override
    public boolean addItem(int index, ItemStack itemStack) {
        return referenceValue().add(index, itemStack.reference());
    }

}
