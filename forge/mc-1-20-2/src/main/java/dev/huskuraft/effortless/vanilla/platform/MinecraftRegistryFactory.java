package dev.huskuraft.effortless.vanilla.platform;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Registry;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.RegistryFactory;
import dev.huskuraft.effortless.vanilla.core.MinecraftBlockState;
import dev.huskuraft.effortless.vanilla.core.MinecraftItem;
import dev.huskuraft.effortless.vanilla.core.MinecraftRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

@AutoService(RegistryFactory.class)
public final class MinecraftRegistryFactory implements RegistryFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PlatformReference> Registry<T> getRegistry(Class<T> clazz) {
        if (clazz == Item.class) return (Registry<T>) new MinecraftRegistry<>(BuiltInRegistries.ITEM, MinecraftItem::ofNullable);
        if (clazz == BlockState.class) return (Registry<T>) new MinecraftRegistry<>(Block.BLOCK_STATE_REGISTRY, MinecraftBlockState::ofNullable);
        return null;
    }


}
