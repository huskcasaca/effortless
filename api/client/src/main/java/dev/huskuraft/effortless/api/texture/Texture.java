package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.util.Set;

public interface Texture {

    ResourceLocation resource();

    Set<ResourceLocation> sprites();

    TextureSprite getSprite(ResourceLocation name);

}
