package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.Effortless;

public abstract class Item {

    public static Item fromId(Resource id) {
        return Effortless.getInstance().getPlatform().newItem(id);
    }

    public abstract ItemStack getDefaultStack();

    public abstract boolean isBlockItem();

    public abstract Resource getId();

}