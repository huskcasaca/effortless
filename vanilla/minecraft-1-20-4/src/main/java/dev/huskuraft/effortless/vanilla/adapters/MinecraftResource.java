package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.core.Resource;
import net.minecraft.resources.ResourceLocation;

public class MinecraftResource extends Resource {

    private final ResourceLocation reference;

    MinecraftResource(ResourceLocation reference) {
        this.reference = reference;
    }

    public static Resource fromMinecraftResource(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        return new MinecraftResource(resourceLocation);
    }

    public static ResourceLocation toMinecraftResource(Resource resource) {
        if (resource == null) {
            return null;
        }
        return ((MinecraftResource) resource).reference;
    }

    @Override
    public String getNamespace() {
        return reference.getNamespace();
    }

    @Override
    public String getPath() {
        return reference.getPath();
    }
}
