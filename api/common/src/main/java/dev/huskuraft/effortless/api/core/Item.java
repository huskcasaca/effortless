package dev.huskuraft.effortless.api.core;

public abstract class Item {

    public static Item fromId(Resource id) {
        return Entrance.getInstance().getPlatform().newItem(id);
    }

    public abstract ItemStack getDefaultStack();

    public abstract boolean isBlockItem();

    public abstract Resource getId();

}