package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ResourceKey;
import dev.huskuraft.effortless.api.core.ResourceLocation;

public record MinecraftResourceKey<T>(
        net.minecraft.resources.ResourceKey<?> refs
) implements ResourceKey<T> {

    @Override
    public ResourceLocation registry() {
        return new MinecraftResourceLocation(refs.registry());
    }

    @Override
    public ResourceLocation location() {
        return new MinecraftResourceLocation(refs.location());
    }

}
