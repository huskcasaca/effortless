package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Inventory;
import dev.huskuraft.effortless.api.core.ItemStack;

public record MinecraftInventory(net.minecraft.world.entity.player.Inventory refs) implements Inventory {

    @Override
    public List<ItemStack> getItems() {
        return refs.items.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getArmorItems() {
        return refs.armor.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getOffhandItems() {
        return refs.offhand.stream().map(MinecraftItemStack::new).collect(Collectors.toList());
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        refs.items.set(index, itemStack.reference());
    }

    @Override
    public void setArmorItem(int index, ItemStack itemStack) {
        refs.armor.set(index, itemStack.reference());
    }

    @Override
    public void setOffhandItem(int index, ItemStack itemStack) {
        refs.offhand.set(index, itemStack.reference());
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        return refs.add(itemStack.reference());
    }

    @Override
    public int getSelected() {
        return refs.selected;
    }

    @Override
    public int getHotbarSize() {
        return refs.getSelectionSize();
    }

}
