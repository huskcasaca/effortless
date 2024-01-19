package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Item extends PlatformReference {

    static Item fromId(ResourceLocation id) {
        return Entrance.getInstance().getPlatform().newItem(id);
    }

    ItemStack getDefaultStack();

    boolean isBlockItem();

    ResourceLocation getId();

}