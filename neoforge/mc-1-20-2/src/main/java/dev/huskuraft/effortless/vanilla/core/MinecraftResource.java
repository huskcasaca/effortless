package dev.huskuraft.effortless.vanilla.core;

import java.io.IOException;

import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.core.ResourceMetadata;

public record MinecraftResource(
        net.minecraft.server.packs.resources.Resource refs
) implements Resource {

    @Override
    public ResourceMetadata metadata() throws IOException {
        var metadata = refs.metadata();
        return () -> metadata;
    }

}
