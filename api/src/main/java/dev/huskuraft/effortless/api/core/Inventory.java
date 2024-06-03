package dev.huskuraft.effortless.api.core;

import java.util.List;

public interface Inventory extends Container {

    default List<ItemStack> getBagItems() {
        return getItems().subList(0, getBagSize());
    }

    default List<ItemStack> getArmorItems() {
        return getItems().subList(getBagSize(), getBagSize() + getArmorSize());
    }

    default List<ItemStack> getOffhandItems() {
        return getItems().subList(getBagSize() + getArmorSize(), getBagSize() + getArmorSize() + getOffhandSize());
    }

    default ItemStack getBagItem(int index) {
        return getItem(index);
    }

    default ItemStack getArmorItem(int index) {
        return getItem(index + getBagSize());
    }

    default ItemStack getOffhandItem(int index) {
        return getItem(index + getBagSize() + getArmorSize());
    }

    default ItemStack getOffhandItem() {
        return getOffhandItem(0);
    }

    default void setOffhandItem(ItemStack itemStack) {
        setOffhandItem(0, itemStack);
    }

    default void setBagItem(int index, ItemStack itemStack) {
        setItem(index, itemStack);
    }

    default void setArmorItem(int index, ItemStack itemStack) {
        setItem(index + getBagSize(), itemStack);
    }

    default void setOffhandItem(int index, ItemStack itemStack) {
        setItem(index + getBagSize() + getArmorSize(), itemStack);
    }

    @Override
    default int getContainerSize() {
        return getBagSize() + getArmorSize() + getOffhandSize();
    }

    int getBagSize();

    int getArmorSize();

    int getOffhandSize();

    int getHotbarSize();

    int getSelected();

    default int getFreeSlotIndex() {
        return getBagItems().stream().filter(ItemStack::isEmpty).findFirst().map(getBagItems()::indexOf).orElse(-1);
    }

    default boolean isHotbarSlot(int index) {
        return index >= 0 && index < getHotbarSize();
    }

    default ItemStack getSelectedItem() {
        if (isHotbarSlot(getSelected())) {
            return getBagItem(getSelected());
        } else {
            return ItemStack.empty();
        }
    }

    default void setSelectedItem(ItemStack itemStack) {
        setBagItem(getSelected(), itemStack);
    }

    default List<ItemStack> getHotbarItems() {
        return getBagItems().subList(0, getHotbarSize());
    }

}
