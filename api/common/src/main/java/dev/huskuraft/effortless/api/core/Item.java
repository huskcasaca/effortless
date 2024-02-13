package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Item extends PlatformReference {

    static Item fromId(ResourceLocation id) {
        return ContentFactory.getInstance().newItem(id);
    }

    ItemStack getDefaultStack();

    boolean isBlockItem();

    ResourceLocation getId();

}
