package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Container extends PlatformReference {

    List<ItemStack> getItems();

    default ItemStack getItem(int index) {
        return getItems().get(index);
    }

    void setItem(int index, ItemStack itemStack);

    default boolean addItem(int index, ItemStack itemStack) {
        var items = getItems();
        for (int indexInContainer = 0; indexInContainer < items.size(); indexInContainer++) {
            var itemStackInContainer = items.get(indexInContainer);
            if (itemStackInContainer.isAir()) {
                setItem(indexInContainer, itemStack);
                return true;
            }
        }
        return false;
    }

    default boolean removeItem(int index) {
        setItem(index, ItemStack.empty());
        return true;
    }

    default boolean addItem(ItemStack itemStack) {
        return addItem(-1, itemStack);
    }

    default int getContainerSize() {
        return getItems().size();
    }

    default boolean contains(ItemStack itemStack) {
        return getItems().contains(itemStack);
    }

}
