package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public class MinecraftResourceLocation implements ResourceLocation {

    private final net.minecraft.resources.ResourceLocation reference;

    public MinecraftResourceLocation(net.minecraft.resources.ResourceLocation reference) {
        this.reference = reference;
    }

    public static ResourceLocation ofNullable(net.minecraft.resources.ResourceLocation reference) {
        return reference == null ? null : new MinecraftResourceLocation(reference);
    }

    @Override
    public Object referenceValue() {
        return reference;
    }

    @Override
    public String getNamespace() {
        return reference.getNamespace();
    }

    @Override
    public String getPath() {
        return reference.getPath();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftResourceLocation resource && reference.equals(resource.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public String toString() {
        return getNamespace() + ":" + getPath();
    }
}
