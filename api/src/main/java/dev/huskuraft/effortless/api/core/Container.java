package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Container extends PlatformReference {

    List<ItemStack> getItems();

    boolean addItem(int index, ItemStack itemStack);

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
