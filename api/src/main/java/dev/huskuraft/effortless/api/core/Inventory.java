package dev.huskuraft.effortless.api.core;

import java.util.List;
import java.util.stream.Stream;

public interface Inventory extends Container {

    default List<ItemStack> getItems() {
        return Stream.of(getBagItems(), getArmorItems(), getOffhandItems()).flatMap(List::stream).toList();
    }

    List<ItemStack> getBagItems();

    List<ItemStack> getArmorItems();

    List<ItemStack> getOffhandItems();

    void setBagItem(int index, ItemStack itemStack);

    void setArmorItem(int index, ItemStack itemStack);

    void setOffhandItem(int index, ItemStack itemStack);

    int getSelected();

    int getHotbarSize();

    default ItemStack getBagItem(int index) {
        return getBagItems().get(index);
    }

    default ItemStack getArmorItem(int index) {
        return getArmorItems().get(index);
    }

    default ItemStack getOffhandItem(int index) {
        return getOffhandItems().get(index);
    }

    default int getFreeSlotIndex() {
        return getBagItems().stream().filter(ItemStack::isEmpty).findFirst().map(getBagItems()::indexOf).orElse(-1);
    }

    default boolean contains(ItemStack itemStack) {
        return getBagItems().contains(itemStack) || getArmorItems().contains(itemStack) || getOffhandItems().contains(itemStack);
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

    default ItemStack getOffhandItem() {
        return getOffhandItems().get(0);
    }

    default void setSelectedItem(ItemStack itemStack) {
        setBagItem(getSelected(), itemStack);
    }

    default void setOffhandItem(ItemStack itemStack) {
        setOffhandItem(0, itemStack);
    }

    default List<ItemStack> getHotbarItems() {
        return getBagItems().subList(0, getHotbarSize());
    }

}
