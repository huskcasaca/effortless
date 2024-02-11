package dev.huskuraft.effortless.api.texture;

import java.util.Set;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record SimpleTexture(
        ResourceLocation resource
) implements Texture {

    @Override
    public Set<ResourceLocation> sprites() {
        return Set.of();
    }

    @Override
    public TextureSprite getSprite(ResourceLocation name) {
        return null;
    }
}
