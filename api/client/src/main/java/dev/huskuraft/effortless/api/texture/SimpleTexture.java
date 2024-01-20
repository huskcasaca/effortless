package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.core.ResourceLocation;

import java.util.Set;

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
