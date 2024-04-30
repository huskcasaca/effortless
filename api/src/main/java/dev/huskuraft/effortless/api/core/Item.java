package dev.huskuraft.effortless.api.core;

import java.util.Optional;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Item extends PlatformReference {

    static Item fromId(ResourceLocation id) {
        return ContentFactory.getInstance().newItem(id);
    }

    static Optional<Item> fromIdOptional(ResourceLocation id) {
        return ContentFactory.getInstance().newOptionalItem(id);
    }

    ItemStack getDefaultStack();

    Block getBlock();

    ResourceLocation getId();

    InteractionResult useOnBlock(Player player, BlockInteraction blockInteraction);

}
