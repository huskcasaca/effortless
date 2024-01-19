package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record SimpleTextureSprite(
        ResourceLocation name,
        ResourceLocation texture,
        int width,
        int height,
        int x,
        int y,
        float u0,
        float u1,
        float v0,
        float v1,
        SpriteScaling scaling
) implements TextureSprite {

    public SimpleTextureSprite(ResourceLocation name, ResourceLocation texture, int width, int height, int x, int y, int textureWidth, int textureHeight, SpriteScaling scaling) {
        this(
                name,
                texture,
                width,
                height,
                x,
                y,
                (float) x / textureWidth,
                (float) (x + width) / textureWidth,
                (float) y / textureHeight,
                (float) (y + height) / textureHeight,
                scaling
        );
    }


}
