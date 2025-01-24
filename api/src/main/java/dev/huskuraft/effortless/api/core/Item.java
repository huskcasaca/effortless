package dev.huskuraft.effortless.api.core;

import java.util.Optional;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.RegistryFactory;
import dev.huskuraft.effortless.api.text.Text;

public interface Item extends PlatformReference {

    Registry<Item> REGISTRY = RegistryFactory.getInstance().getRegistry();

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

    boolean isCorrectToolForDrops(BlockState blockState);

    int getMaxStackSize();

    int getMaxDamage();

    boolean mineBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack);

    Text getName(ItemStack itemStack);

    default Text getName() {
        return getName(getDefaultStack());
    }

    default boolean isCorrectToolForDropsNoThrows(BlockState blockState) {
        try {
            return isCorrectToolForDrops(blockState);
        } catch (Throwable t) {
            return false;
        }
    }

}
