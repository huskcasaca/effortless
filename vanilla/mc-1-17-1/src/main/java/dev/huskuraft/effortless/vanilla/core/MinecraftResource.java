package dev.huskuraft.effortless.vanilla.core;

import java.io.IOException;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.ResourceMetadata;
import dev.huskuraft.effortless.vanilla.platform.MinecraftResourceLocation;

public class MinecraftResource implements Resource {

    private final net.minecraft.server.packs.resources.Resource resource;
    private final net.minecraft.resources.ResourceLocation location;

    public MinecraftResource(net.minecraft.server.packs.resources.Resource resource, net.minecraft.resources.ResourceLocation location) {
        this.resource = resource;
        this.location = location;
    }

    @Override
    public net.minecraft.server.packs.resources.Resource referenceValue() {
        return resource;
    }

    @Override
    public ResourceLocation location() {
        return new MinecraftResourceLocation(location);
    }

    @Override
    public ResourceMetadata metadata() throws IOException {
        var metadata = resource.getMetadata(null); // FIXME: 23/1/24
        return () -> metadata;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftResource resource && this.resource.equals(resource.resource);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }
}
