package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Inventory extends PlatformReference {

    List<ItemStack> getItems();

    List<ItemStack> getArmors();

    List<ItemStack> getOffhand();

    void setItem(int index, ItemStack itemStack);

    ItemStack getItem(int index);

    boolean addItem(ItemStack itemStack);

    boolean addItem(int index, ItemStack itemStack);

    default int getFreeSlotIndex() {
        return getItems().stream().filter(ItemStack::isEmpty).findFirst().map(getItems()::indexOf).orElse(-1);
    }

    default int getSlotSize() {
        return getItems().size() + getArmors().size() + getOffhand().size();
    }

    default boolean contains(ItemStack itemStack) {
        return getItems().contains(itemStack);
    }

}
