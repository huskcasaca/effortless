package dev.huskuraft.effortless.vanilla.platform;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Registry;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.RegistryFactory;
import dev.huskuraft.effortless.vanilla.core.MinecraftItem;
import dev.huskuraft.effortless.vanilla.core.MinecraftRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

@AutoService(RegistryFactory.class)
public final class MinecraftRegistryFactory implements RegistryFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PlatformReference> Registry<T> getRegistry(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        var clazz = (Class<T>) typeGetter.getClass().getComponentType();
        if (clazz == Item.class) {
            return (Registry<T>) new MinecraftRegistry<>(
                    BuiltInRegistries.ITEM,
                    MinecraftItem::new
            );
        }

        throw new IllegalArgumentException("Unknown registry: " + clazz.getName());
    }


}
