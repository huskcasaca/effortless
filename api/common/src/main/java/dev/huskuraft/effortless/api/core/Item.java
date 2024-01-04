package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Entrance;

public abstract class Item {

    public static Item fromId(Resource id) {
        return Entrance.getInstance().getPlatform().newItem(id);
    }

    public abstract ItemStack getDefaultStack();

    public abstract boolean isBlockItem();

    public abstract Resource getId();

}