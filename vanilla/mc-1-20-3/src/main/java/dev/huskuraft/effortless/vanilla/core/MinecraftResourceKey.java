package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ResourceKey;
import dev.huskuraft.effortless.api.core.ResourceLocation;

public class MinecraftResourceKey<T> implements ResourceKey<T> {

    private final net.minecraft.resources.ResourceKey<?> reference;

    public MinecraftResourceKey(net.minecraft.resources.ResourceKey<?> reference) {
        this.reference = reference;
    }

    @Override
    public Object referenceValue() {
        return reference;
    }

    @Override
    public ResourceLocation registry() {
        return new MinecraftResourceLocation(reference.registry());
    }

    @Override
    public ResourceLocation location() {
        return new MinecraftResourceLocation(reference.location());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftResourceKey<?> obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public String toString() {
        return reference.toString();
    }
}
