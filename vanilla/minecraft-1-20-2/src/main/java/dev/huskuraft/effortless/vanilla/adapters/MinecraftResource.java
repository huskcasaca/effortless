package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Resource;
import net.minecraft.resources.ResourceLocation;

class MinecraftResource extends Resource {

    private final ResourceLocation resourceLocation;

    MinecraftResource(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
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
