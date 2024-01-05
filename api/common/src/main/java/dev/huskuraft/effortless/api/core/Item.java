package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Entrance;

public interface Item {

    static Item fromId(Resource id) {
        return Entrance.getInstance().getPlatform().newItem(id);
    }

    ItemStack getDefaultStack();

    boolean isBlockItem();

    Resource getId();

}