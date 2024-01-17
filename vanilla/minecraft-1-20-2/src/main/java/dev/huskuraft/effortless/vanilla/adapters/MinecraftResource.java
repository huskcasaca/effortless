package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.Resource;
import net.minecraft.resources.ResourceLocation;

public class MinecraftResource implements Resource {

    private final ResourceLocation reference;

    public MinecraftResource(ResourceLocation reference) {
        this.reference = reference;
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
        return obj instanceof MinecraftResource resource && reference.equals(resource.reference);
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
