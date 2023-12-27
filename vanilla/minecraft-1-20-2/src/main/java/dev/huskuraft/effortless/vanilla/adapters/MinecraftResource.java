package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Resource;
import net.minecraft.resources.ResourceLocation;

public class MinecraftResource extends Resource {

    private final ResourceLocation resourceLocation;

    MinecraftResource(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
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
        if (resource instanceof MinecraftResource minecraftResource) {
            return minecraftResource.getRef();
        }
        return new ResourceLocation(resource.getNamespace(), resource.getPath());
    }

    public ResourceLocation getRef() {
        return resourceLocation;
    }

    @Override
    public String getNamespace() {
        return getRef().getNamespace();
    }

    @Override
    public String getPath() {
        return getRef().getPath();
    }
}
