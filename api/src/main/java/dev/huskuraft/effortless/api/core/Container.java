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

    default boolean addItem(ItemStack itemStack) {
        var items = getItems();
        for (int i = 0; i < items.size(); i++) {
            var inContainer = items.get(i);
            if (inContainer.isEmpty()) {
                setItem(i, itemStack);
                return true;
            }
        }
        return false;
    }

    default boolean removeItem(int index) {
        setItem(index, ItemStack.empty());
        return true;
    }

    default boolean contains(ItemStack itemStack) {
        return getItems().contains(itemStack);
    }

    default int indexOf(ItemStack itemStack) {
        return getItems().indexOf(itemStack);
    }

}
