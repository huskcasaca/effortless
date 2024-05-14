package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Inventory extends PlatformReference {

    List<ItemStack> getItems();

    List<ItemStack> getArmorItems();

    List<ItemStack> getOffhandItems();

    void setItem(int index, ItemStack itemStack);

    void setArmorItem(int index, ItemStack itemStack);

    void setOffhandItem(int index, ItemStack itemStack);

    boolean addItem(ItemStack itemStack);

    int getSelected();

    int getHotbarSize();

    default ItemStack getItem(int index) {
        return getItems().get(index);
    }

    default ItemStack getArmorItem(int index) {
        return getArmorItems().get(index);
    }

    default ItemStack getOffhandItem(int index) {
        return getOffhandItems().get(index);
    }

    default int getFreeSlotIndex() {
        return getItems().stream().filter(ItemStack::isEmpty).findFirst().map(getItems()::indexOf).orElse(-1);
    }

    default int getSlotSize() {
        return getItems().size() + getArmorItems().size() + getOffhandItems().size();
    }

    default boolean contains(ItemStack itemStack) {
        return getItems().contains(itemStack);
    }

    default boolean isHotbarSlot(int index) {
        return index >= 0 && index < getHotbarSize();
    }

    default ItemStack getSelectedItem() {
        if (isHotbarSlot(getSelected())) {
            return getItem(getSelected());
        } else {
            return ItemStack.empty();
        }
    }


    default ItemStack getOffhandItem() {
        return getOffhandItems().get(0);
    }

    default List<ItemStack> getHotbarItems() {
        return getItems().subList(0, getHotbarSize());
    }


}
