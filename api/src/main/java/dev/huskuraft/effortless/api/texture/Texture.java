package dev.huskuraft.effortless.api.texture;

import java.util.Set;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface Texture {

    ResourceLocation resource();

    Set<ResourceLocation> sprites();

    TextureSprite getSprite(ResourceLocation name);

}
