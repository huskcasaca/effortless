package dev.huskuraft.effortless.building;

import java.util.List;

import dev.huskuraft.effortless.api.core.Inventory;
import dev.huskuraft.effortless.api.core.ItemStack;

public record InventorySnapshot(
        List<ItemStack> items,
        List<ItemStack> armorItems,
        List<ItemStack> offhandItems,
        int selected,
        int hotbarSize
) implements Inventory {

    public InventorySnapshot(Inventory inventory) {
        this(inventory.getItems(), inventory.getArmorItems(), inventory.getOffhandItems(), inventory.getSelected(), inventory.getHotbarSize());
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public List<ItemStack> getArmorItems() {
        return armorItems;
    }

    @Override
    public List<ItemStack> getOffhandItems() {
        return offhandItems;
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArmorItem(int index, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOffhandItem(int index, ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addItem(ItemStack itemStack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSelected() {
        return selected;
    }

    @Override
    public int getHotbarSize() {
        return hotbarSize;
    }

    @Override
    public InventorySnapshot refs() {
        return this;
    }
}
