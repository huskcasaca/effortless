package dev.huskuraft.effortless.api.core;

import java.util.List;
import java.util.stream.IntStream;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Container extends PlatformReference {

    ItemStack getItem(int index);

    void setItem(int index, ItemStack itemStack);

    int getContainerSize();

    default List<ItemStack> getItems() {
        return IntStream.range(0, getContainerSize()).mapToObj(this::getItem).toList();
    }

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

    default boolean contains(ItemStack itemStack) {
        return getItems().contains(itemStack);
    }

}
