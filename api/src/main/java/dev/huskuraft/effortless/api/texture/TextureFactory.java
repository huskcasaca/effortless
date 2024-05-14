package dev.huskuraft.effortless.api.texture;

import dev.huskuraft.effortless.api.platform.PlatformLoader;

public interface TextureFactory {

    static TextureFactory getInstance() {
        return PlatformLoader.getSingleton();
    }

    Texture getBlockAtlasTexture();

    TextureSprite getBackgroundTextureSprite();

    TextureSprite getButtonTextureSprite(boolean enabled, boolean focused);

    TextureSprite getDemoBackgroundTextureSprite();

}
